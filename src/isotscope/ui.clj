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


(ns isotscope.ui)
(import javax.swing.JFrame)
(import javax.swing.JEditorPane)
(import java.awt.Color)


(defn setup-frame [app]
  (let [cont-pane (.getContentPane app)]
  (.setSize app 500 500)
  (.setVisible app true)
  (.setDefaultCloseOperation app JFrame/EXIT_ON_CLOSE)
  ;;(.add cont-pane (JEditorPane.))
  (.setBackground cont-pane Color/BLACK)
  )
  )

(defn ui-main [] (let [app (JFrame.)]
                 (setup-frame app)
                 app))











;;
