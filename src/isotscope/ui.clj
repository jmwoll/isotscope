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
(import javax.swing.JTextArea)
(import javax.swing.JLabel)
(import java.awt.Color)
(import java.awt.GridBagLayout)
(import java.awt.GridBagConstraints)

(defn setup-frame [app]
  (let [cont-pane (.getContentPane app) editor (JTextArea. 5 20)
        results-editor (JTextArea. 5 20)
        back-col Color/BLACK
        edit-col (Color. 20 20 50)
        grid1 (GridBagConstraints.)
        ]
  (.setSize app 500 500)
  (.setVisible app true)
  (.setDefaultCloseOperation app JFrame/EXIT_ON_CLOSE)
  (.setLayout cont-pane (GridBagLayout.))
  (set! (.gridx grid1) 0)
  (set! (.gridy grid1) 1)
  (set! (.fill grid1) GridBagConstraints/HORIZONTAL)
  (.add cont-pane editor grid1)
  (.setBackground editor edit-col)
  (.setForeground editor Color/WHITE)
  (set! (.gridx grid1) 0)
  (set! (.gridy grid1) 0)
  (.add cont-pane (JLabel. "Sum Formula") grid1)
  (set! (.gridx grid1) 0)
  (set! (.gridy grid1) 2)
  (.add cont-pane (JLabel. "Isotopic Pattern") grid1)
  (set! (.gridx grid1) 0)
  (set! (.gridy grid1) 3)
  (.setBackground results-editor edit-col)
  (.setForeground results-editor Color/WHITE)
  (.add cont-pane results-editor grid1)
  (.setBackground cont-pane back-col)
  )
  )

(defn ui-main [] (let [app (JFrame.)]
                 (setup-frame app)
                 app))











;;
