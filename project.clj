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

(defproject isotscope "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "GPL3"
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.8.0"],
                 [org.openscience.cdk/cdk-bundle "2.0"]
                 [uk.ac.cam.ch.opsin/opsin-core "2.3.1"]
                 [cheshire "5.8.0"]
                 ]
  :main ^:skip-aot isotscope.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
