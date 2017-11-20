;; Copyright (c) 2017 Jan Wollschläger
;;
;; This program and the accompanying materials are made
;; available under the terms of the Eclipse Public License 2.0
;; which is available at https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns isotscope.isotope
  (:gen-class)
  (:require [clojure.walk :refer :all])
  (:require [cheshire.core :refer :all])
  )

  (import java.util.Date)
  ;;(import uk.ac.cam.ch.wwmm.opsin.NameToStructure)


  (println "running isotope")


  (defn round2
    "Round a double to the given precision (number of significant digits)"
    [precision d]
    (let [factor (Math/pow 10 precision)]
      (/ (Math/round (* d factor)) factor)))


  (defn all-isotopes-str []
    (slurp (.getPath (clojure.java.io/resource "all_isotopes.json"))))

  (defn compute-all-isotopes-dict []
      (clojure.walk/keywordize-keys (cheshire.core/parse-string (all-isotopes-str))))

  (def all-isotopes-dict (memoize compute-all-isotopes-dict))

  (defn isotopes-of [symbol]
    (get (get (all-isotopes-dict) symbol) :isotopes))

  (defn isotopes-nth-property [symbol n]
    (to-array (map (fn [x] (nth x n)) (isotopes-of symbol))))

  (defn isotopes-nom-mass [symbol]
    (isotopes-nth-property symbol 0))

  (defn isotopes-mass [symbol]
    (isotopes-nth-property symbol 1))

  (defn isotopes-prob [symbol]
    (isotopes-nth-property symbol 2))

  ;;(defn parseToCML []
  ;;  (let [nts (NameToStructure/getInstance)] (.parseToCML "acetonitrile" nts)))



  ;; section for random isopat generation
  (defn rand-choice [slices]
    (let [total (reduce + slices)
          r (rand total)]
      (loop [i 0 sum 0]
        (if (< r (+ (nth slices i) sum))
          i
          (recur (inc i) (+ (nth slices i) sum))))))

  (defn rand-isotope-index [symbol]
    (rand-choice (isotopes-prob symbol)))

  (defn take-random-isotope-mass [symbol]
    (nth (isotopes-mass symbol) (rand-isotope-index symbol)))

  (defn rand-isopat-path [sf]
    (reduce +
      (flatten
        (map (fn [kv]
          (take (second kv)
          (repeatedly #(take-random-isotope-mass (first kv))))) sf))))

  (defn rand-isopat-gen [sf num-trials]
    ;; floating point precision of 6 or 2?
    (frequencies (map (partial round2 1)(take num-trials (repeatedly
      #(rand-isopat-path sf))))))
