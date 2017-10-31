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

(ns isotscope.isotope-test
  (:require [clojure.test :refer :all]
            [isotscope.isotope :refer :all]
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
    (is (true? (rand-isopat-gen sf)))
    )
  )
