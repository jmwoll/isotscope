;; Copyright (c) 2017 Jan Wollschläger
;;
;; This program and the accompanying materials are made
;; available under the terms of the Eclipse Public License 2.0
;; which is available at https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns isotscope.core
  (:gen-class)
  (:require [clojure.walk :refer :all])
  (:require [cheshire.core :refer :all])
  (:require [isotscope.ui])
  (:require [isotscope.parser])
  )

;;(isotscope.ui/ui-main) ???
(defn -main
  "Isotscope --- a IUPAC aware isotopic pattern generator"
  [& args] (isotscope.ui/ui-main))
