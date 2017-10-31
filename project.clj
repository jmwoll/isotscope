(defproject isotscope "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "GPL3"
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.8.0"],
                 [org.openscience.cdk/cdk-bundle "2.0"]
                 [uk.ac.cam.ch.opsin/opsin-core "2.3.1"]
                 [cheshire "5.8.0"]]
  :main ^:skip-aot isotscope.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
