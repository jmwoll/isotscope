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

(ns isotscope.parser
(:require [isotscope.isotope])
  )



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


(defn parse-sf-token [token]
  (let [symb (symbol-of-token token)]
  (if (contains? (isotscope.isotope/all-isotopes-dict) symb)
    [symb (count-of-token token)]
    [:Tc 11]
  )
  )
)
