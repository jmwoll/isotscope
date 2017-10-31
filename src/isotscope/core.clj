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

(ns isotscope.core
  (:gen-class)
  (:require [cheshire.core])
  (:require [clojure.walk]))

(import java.util.Date)
(import uk.ac.cam.ch.wwmm.opsin.NameToStructure)


(println "HUURAY")


(defn all-isotopes-str []
  (slurp (.getFile (clojure.java.io/resource "all_isotopes.json"))))


(defn all-isotopes-dict []
  (clojure.walk/keywordize-keys (cheshire.core/parse-string (all-isotopes-str))))



(defn parseToCML []
  (let [nts (NameToStructure/getInstance)] (.parseToCML "acetonitrile" nts)))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
