;; Copyright (c) 2017 Jan Wollschläger
;;
;; This program and the accompanying materials are made
;; available under the terms of the Eclipse Public License 2.0
;; which is available at https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns isotscope.uihelpers
  (:gen-class
    ;; :name com.isot.DocListener
    :init constructor
    :name isotscope.uihelpers.DocListener
    :implements [javax.swing.event.DocumentListener]
    :constructors {[Object] []}
    :state callback
    ) ;; ONLY WORKS IF COMPILING AT REPL with (compile 'isotscope.uihelpers)
  )

;;(import javax.swing.event.DocumentListener)

(defn -handleUpdate [this evt]
  ((.callback this))
  )

(defn -removeUpdate [this evt]
  (-handleUpdate this evt)
  )

(defn -insertUpdate [this evt]
  (-handleUpdate this evt)
  )

(defn -changedUpdate [this evt]
  (-handleUpdate this evt)
  )

(defn -constructor [on-update]
  (println "constructing class")
  ;;(println "handler" on-update)
  [[] on-update]
  )
