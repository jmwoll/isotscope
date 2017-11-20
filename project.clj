;; Copyright (c) 2017 Jan Wollschläger
;;
;; This program and the accompanying materials are made
;; available under the terms of the Eclipse Public License 2.0
;; which is available at https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(defproject isotscope "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0"}
  :dependencies [[org.clojure/clojure "1.8.0"],
                 ;;[org.openscience.cdk/cdk-bundle "2.0"]
                 [uk.ac.cam.ch.opsin/opsin-core "2.3.1"]
                 [cheshire "5.8.0"]
                 ]
  :resource-paths ["resources"];;??
  :main isotscope.core ;; before :main ^:skip-aot isotscope.core
  :aot :all ;; aot needed for class compilation in uihelpers
  :target-path "target/%s"
  )
  ;;:profiles {:uberjar {:aot :all}})
