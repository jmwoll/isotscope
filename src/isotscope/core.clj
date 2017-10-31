(ns isotscope.core
  (:gen-class)
  (:require [cheshire.core :refer :all]))

(import java.util.Date)
(import uk.ac.cam.ch.wwmm.opsin.NameToStructure)


(println "HUURAY")


(defn all-isotopes-str []
  (slurp (.getFile (clojure.java.io/resource "all_isotopes.json"))))


(defn all-isotopes-dict []
  (parse-string all-isotopes-str))



(defn parseToCML []
  (let [nts (NameToStructure/getInstance)] (.parseToCML "acetonitrile" nts)))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
