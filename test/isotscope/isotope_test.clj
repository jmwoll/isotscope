;; Copyright (c) 2017 Jan Wollschläger
;;
;; This program and the accompanying materials are made
;; available under the terms of the Eclipse Public License 2.0
;; which is available at https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns isotscope.isotope-test
  (:require [clojure.test :refer :all]
            [isotscope.isotope :refer :all]))

(deftest a-test
  (testing "Check (= 1 1)"
    (is (= 1 1))))


(deftest test-all-isotopes-str
  (testing "all-isotopes-str"
    (let [rslt (all-isotopes-str)]
    (is (.contains rslt "Hydrogen"))
    (is (.contains rslt "Deuterium"))
    (is (.contains rslt "Meitnerium")))))


(deftest test-all-isotopes-dict
  (testing "all-isotopes-dict"
    (let [rslt (all-isotopes-dict)]
      (is (contains? rslt :H))
      (is (contains? rslt :D))
      (is (contains? rslt :Mt))
      (is (contains? rslt :C))
      (is (contains? rslt :N))
      (is (contains? rslt :O))
    )))


(deftest test-isotopes-nom-mass
  (testing "isotopes-nom-mass")
  (let [rslt (isotopes-nom-mass :C)]
    (is (= 2 (alength rslt)))
    (is (= 12 (nth rslt 0)))
  )
  )


(deftest test-rand-isopat-gen
  (testing "rand-isopat-gen")
  (let [sf {:C 10 :H 10}]
    (is (< 2 (count (rand-isopat-gen sf 10000))))
    )
  )
