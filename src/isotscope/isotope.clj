;; Copyright (C) 2017  Jan Wollschläger <jmw.tau@gmail.com>
;; This file is part of Isotscope.
;;
;; Isotscope is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.
;;
;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.
;;
;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see <http://www.gnu.org/licenses/>.

(ns isotscope.isotope
  (:gen-class)
  (:require [cheshire.core])
  (:require [clojure.walk]))

(import java.util.Date)
(import uk.ac.cam.ch.wwmm.opsin.NameToStructure)


(println "HUURAY")


(defn round2
  "Round a double to the given precision (number of significant digits)"
  [precision d]
  (let [factor (Math/pow 10 precision)]
    (/ (Math/round (* d factor)) factor)))


(defn all-isotopes-str []
  (slurp (.getFile (clojure.java.io/resource "all_isotopes.json"))))

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

(defn parseToCML []
  (let [nts (NameToStructure/getInstance)] (.parseToCML "acetonitrile" nts)))



;; random isopat generation
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
  (nth (isotopes-mass symbol) (rand-isotope-index symbol))
  )

(defn rand-isopat-path [sf]
  (reduce +
    (flatten
      (map (fn [kv]
        (take (second kv)
        (repeatedly #(take-random-isotope-mass (first kv))))) sf))))

(defn rand-isopat-gen [sf]
  (frequencies (map (partial round2 6)(take 10000 (repeatedly
    #(rand-isopat-path sf)))))
  )
