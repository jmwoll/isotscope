(ns isotscope.core-test
  (:require [clojure.test :refer :all]
            [isotscope.core :refer :all]))

(deftest a-test
  (testing "Check (= 1 1)"
    (is (= 1 1))))


(deftest test-all-isotopes-str
  (testing "all-isotopes-str"
    (let [rslt (all-isotopes-str)]
    (is (.contains rslt "Hydrogen"))
    (is (.contains rslt "Deuterium"))
    (is (.contains rslt "Meitnerium")))))
