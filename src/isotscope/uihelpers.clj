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















;;
