;; Copyright (c) 2018 Jan Wollschläger
;;
;; This program and the accompanying materials are made
;; available under the terms of the Eclipse Public License 2.0
;; which is available at https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns isotscope.parser
(:require [isotscope.isotope])
(:require [clojure.walk :refer :all])
(:require [clojure.string :as cljstr])
  )


;; for smiles parsing
(import org.openscience.cdk.smiles.SmilesParser)
(import org.openscience.cdk.DefaultChemObjectBuilder)
(import org.openscience.cdk.tools.CDKHydrogenAdder)
(import org.openscience.cdk.atomtype.CDKAtomTypeMatcher)
(import org.openscience.cdk.tools.manipulator.AtomTypeManipulator)
(import org.openscience.cdk.tools.manipulator.AtomContainerManipulator)

;; for iupac parsing
(import uk.ac.cam.ch.wwmm.opsin.NameToStructure)




;; The task of the parser is to transform the
;; input string from a raw input containing
;; both iupac and sum formula declarations into a list
;; of lists of the type [[symbol-of-atom count-of-atom]]:
;;  C11 H22 O33 -> [C11 H22 O33] -> [[:C 11] [:H 22] [:O 33]]
;;  -> {:C 11 :H 22 :O 33}
;;


;; Tokens are *white-space separated* words.
(defn tokenize-sf-string [sf]
  (filter not-empty (clojure.string/split sf #"\s+")))


(defn symbol-of-token [tok]
  (keyword (first (filter not-empty (clojure.string/split tok #"\d")))))

(defn count-of-token [tok]
  (let [number (filter not-empty (clojure.string/split tok #"\D"))]
  (if (= 1 (count number)) (Integer. (first number)) 1)))

(defn assoc-or-add [d k v]
    (if (contains? d k) (assoc d k (+ v (d k))) (assoc d k v)))

(defn add-sf-dicts [d1 d2]
    (let [hd (first d2)]
    (if (empty? d2) d1
      (add-sf-dicts (assoc-or-add d1 (first hd) (second hd)) (rest d2)))
    )
  )


(defn iupac-name-to-cml [name]
  (let [nts (NameToStructure/getInstance)] (.parseToCML nts name)))

;; e.g. 1 => {:+ 1, :- 0}
(defn to-charge-dict [charge]
    {:+ (if (< charge 0) 0 charge) :- (if (> charge 0) 0 (- charge))}
  )

(defn cml-to-charge [cml]
  ;; allowing a maximum charge of +-99 seems reasonable
  (let [elt-lst (re-seq #"formalCharge=\"(\-?[0-9][0-9]?)\"" cml)]
  (reduce (fn [a b] (+ a (Integer. (second b)))) 0 elt-lst)
  )
  )

(defn cml-to-sf [cml]
   (let [elt-lst (re-seq #"elementType=\"([a-zA-Z][a-zA-Z]?)\"" cml)
         sf (clojure.walk/keywordize-keys (frequencies (map (fn [itm] (second itm)) elt-lst)))
         charge (cml-to-charge cml)
         charge-dct (to-charge-dict charge)
         ]
         (add-sf-dicts sf charge-dct)
         )
)

(defn smiles-to-mol [smi]
    (let [sp (SmilesParser. (DefaultChemObjectBuilder/getInstance))]
      (.parseSmiles sp smi)
    )
  )

(defn add-hydrogens [mol]
  (AtomContainerManipulator/convertImplicitToExplicitHydrogens mol)
  mol
)

(defn add-hydrogens-old-2 [mol]
    (def matcher (CDKAtomTypeMatcher/getInstance (.getBuilder mol)))
    (doseq [atom (.atoms mol)]
      (println "-->")
      (def atom-type (.findMatchingAtomType matcher mol atom))
      (AtomTypeManipulator/configure atom atom-type)
    )
    (.addImplicitHydrogens (CDKHydrogenAdder/getInstance (.getBuilder mol)) mol)
    mol
)

(defn add-hydrogens-old [mol]
  (def rslt (.addImplicitHydrogens (CDKHydrogenAdder/getInstance (DefaultChemObjectBuilder/getInstance)) mol))
  (if (= rslt nil) (println "!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*!*"))
  mol
)

(defn mol-to-charge [mol]
  (reduce + (map (fn [a] (.getFormalCharge a)) (.atoms mol)))
  )

(defn mol-to-sf [mol]
  (let [atoms (.atoms mol)
        sf (map (fn [[k v]] [(keyword k) v]) (seq (frequencies (map (fn [itm] (.getSymbol itm)) atoms))))
        ]
        (add-sf-dicts (into (sorted-map) sf) (to-charge-dict (mol-to-charge mol)))
    )
)

(defn smiles-to-sf [smi]
  (def smi (if (= (nth smi 0) "$") (subs smi 1) smi))
  (mol-to-sf (add-hydrogens (smiles-to-mol smi)))
)


(defn to-pairs [lst]
  (if (empty? lst) []
  (let [hd [(first lst) (second lst)]
        tl (rest (rest lst))]
        (if (empty? tl)
          [hd]
          (cons hd (to-pairs tl))
        ))
  ))

(defn parse-sf-token [token]
  (defn token-charge-count [tok] (if (cljstr/blank? tok) 1 (Integer. tok)))
  (let [symb (symbol-of-token token)]
  (if (or (= symb (keyword "+")) (= symb (keyword "-"))) [symb (token-charge-count (subs token 1))]
    ;; else, if it is not a charge token:
    (if (contains? (isotscope.isotope/all-isotopes-dict) symb)
      [symb (count-of-token token)]
      ;;[:Tc 11] ;; for now, handle IUPAC later -> actually raise error here!
      (try (seq (cml-to-sf (iupac-name-to-cml token)))
      (catch Exception e
        (println "TRYING SMILES")
        (try (seq (smiles-to-sf token))
        (catch Exception e
          (println ">>" e)
          [:NA :NA]));; inner try
        ));; outer try
    );; inner if
  )))

(defn to-tokens [toks]
    ;;(map (fn [word] [(symbol-of-token word) (count-of-token word)]) words))
    (to-pairs (flatten (map parse-sf-token toks))))



(defn parse-sf-tokens [tokens]
  (if (empty? tokens) {}
    (let [hd (first tokens)
        tl (rest tokens)]
        (assoc-or-add (parse-sf-tokens tl) (first hd) (second hd))
    )))

(defn parse-sf-string [sf-str]
  (parse-sf-tokens (to-tokens (tokenize-sf-string sf-str))))



(defn to-percentage-sf [sf]
  (defn above-min-perc [itm] (> (second itm) 0.0))
  (let [sum-sf (reduce + (map second sf))]
  (filter above-min-perc (map (fn [itm] [(first itm)
    ((partial isotscope.isotope/round2 2) (float (/ (second itm) sum-sf)))]) sf)
  )
  ))

(defn pretty-print-sf [sf]
  (let [sf (dissoc (dissoc sf (keyword "+")) (keyword "-"))]
    (clojure.string/join "\n"
      (map (fn [itm]
        (clojure.string/join "\t" [(first itm) (second itm)])) (sort (to-percentage-sf sf))))
  ))
