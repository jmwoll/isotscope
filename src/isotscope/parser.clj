;; Copyright (c) 2017 Jan Wollschläger
;;
;; This program and the accompanying materials are made
;; available under the terms of the Eclipse Public License 2.0
;; which is available at https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns isotscope.parser
(:require [isotscope.isotope])
(:require [clojure.walk :refer :all])
  )

(import uk.ac.cam.ch.wwmm.opsin.NameToStructure)

;; C11 H22 O33 -> [C11 H22 O33] -> [[:C 11] [:H 22] [:O 33]]
;; -> {:C 11 :H 22 :O 33}

(defn tokenize-sf-string [sf]
  (filter not-empty (clojure.string/split sf #"\s+"))
  )


(defn symbol-of-token [tok]
  (keyword (first (filter not-empty (clojure.string/split tok #"\d"))))
)

(defn count-of-token [tok]
  (let [number (filter not-empty (clojure.string/split tok #"\D"))]
  (if (= 1 (count number)) (Integer. (first number)) 1)
))


(defn iupac-name-to-cml [name]
  (let [nts (NameToStructure/getInstance)] (.parseToCML nts name)))


(defn cml-to-sf [cml]
   (let [elt-lst (re-seq #"elementType=\"([a-zA-Z][a-zA-Z]?)\"" cml)]
   (clojure.walk/keywordize-keys (frequencies (map (fn [itm] (second itm)) elt-lst)))
   )
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
  (let [symb (symbol-of-token token)]
  (if (contains? (isotscope.isotope/all-isotopes-dict) symb)
    [symb (count-of-token token)]
    ;;[:Tc 11] ;; for now, handle IUPAC later -> actually raise error here!
    (try (seq (cml-to-sf (iupac-name-to-cml token)))
    (catch Exception e [:NA :NA])
    )
  )))

(defn to-tokens [toks]
    ;;(map (fn [word] [(symbol-of-token word) (count-of-token word)]) words))
    (to-pairs (flatten (map parse-sf-token toks))))

(defn assoc-or-add [d k v]
  (if (contains? d k) (assoc d k (+ v (d k))) (assoc d k v)))

(defn parse-sf-tokens [tokens]
  (if (empty? tokens) {}
    (let [hd (first tokens)
        tl (rest tokens)]
        (assoc-or-add (parse-sf-tokens tl) (first hd) (second hd))
    )))


(defn parse-sf-string [sf-str]
  (parse-sf-tokens (to-tokens (tokenize-sf-string sf-str))))


(defn add-sf-dicts [d1 d2]
    (let [hd (first d2)]
    (if (empty? d2) d1
      (add-sf-dicts (assoc-or-add d1 (first hd) (second hd)) (rest d2)))
    )
  )

(defn to-percentage-sf [sf]
  (defn above-min-perc [itm] (> (second itm) 0.0))
  (let [sum-sf (reduce + (map second sf))]
  (filter above-min-perc (map (fn [itm] [(first itm)
    ((partial isotscope.isotope/round2 2) (float (/ (second itm) sum-sf)))]) sf)
  )
  ))

(defn pretty-print-sf [sf]
  (clojure.string/join "\n"
    (map (fn [itm]
      (clojure.string/join "\t" [(first itm) (second itm)])) (sort (to-percentage-sf sf))))
  )
