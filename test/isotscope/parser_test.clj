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

(ns isotscope.parser-test
  (:require [clojure.test :refer :all]
            [isotscope.parser :refer :all]))


(deftest test-tokenize-sf-string
  (testing "tokenize-sf-string")
  (let [sf "C10 H10"]
    (is (= ["C10" "H10"] (tokenize-sf-string sf))))
  (let [sf "C10 H10\nC20\tH20\tN30\tO40"]
    (is (= ["C10" "H10" "C20" "H20" "N30" "O40"] (tokenize-sf-string sf))))
  (let [sf "C10 H10\n\n\t\n"]
    (is (= ["C10" "H10"] (tokenize-sf-string sf))))
  (let [sf "\n\t\t\nC10 H10"]
    (is (= ["C10" "H10"] (tokenize-sf-string sf))))
  (let [sf "C10\n\t\t\n\n\nH10"]
    (is (= ["C10" "H10"] (tokenize-sf-string sf))))
  (let [sf "C10\n\r\nH10"]
    (is (= ["C10" "H10"] (tokenize-sf-string sf)))))

(deftest test-symbol-of-token
  (testing "symbol-of-token")
  (let [tok "C11"]
    (is (= :C (symbol-of-token tok)))
  )
  (let [tok "Cl1010"]
    (is (= :Cl (symbol-of-token tok)))
  )
  (let [tok "Br1"]
    (is (= :Br (symbol-of-token tok)))
  ))

(deftest test-count-of-token
  (testing "count-of-token")
  (let [tok "C11"]
    (is (= 11 (count-of-token tok)))
  )
  (let [tok "Cl1010"]
    (is (= 1010 (count-of-token tok)))
  )
  (let [tok "Br1"]
    (is (= 1 (count-of-token tok)))
  )
  (let [tok "C"]
    (is (= 1 (count-of-token tok)))
  )
  (let [tok "Cl"]
    (is (= 1 (count-of-token tok)))
  )
  (let [tok "Br"]
    (is (= 1 (count-of-token tok)))
  ))

(deftest test-parse-sf-token
  (testing "parse-sf-token")
  (let [sf "Cl20"]
    (is (= [:Cl 20] (parse-sf-token sf)))
  )
  (let [sf "Br20"]
    (is (= [:Br 20] (parse-sf-token sf)))
  )
  (let [sf "H"]
    (is (= [:H 1] (parse-sf-token sf)))
  )
  (let [sf "hydrochinon"]
    (is (= [:Tc 11] (parse-sf-token sf))) ;; for now
  )
)

(deftest test-parse-sf-tokens
  (testing "parse-sf-tokens")
  (let [sf [[:C 10] [:H 10]]]
    (is (= {:C 10 :H 10} (parse-sf-tokens sf)))
    ))


(deftest test-parse-sf-string
  (testing "parse-sf-string")
  (let [sf "C10 H10"]
    (is (= {:C 10 :H 10} (parse-sf-string sf)))
    ))
