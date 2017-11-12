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


(ns isotscope.ui
  (:require [isotscope.uihelpers :refer :all])
  (:require [isotscope.parser :refer :all])
  (:require [isotscope.isotope :refer :all])
  )
(import javax.swing.JFrame)
(import javax.swing.JPanel)
(import javax.swing.JTextArea)
(import javax.swing.JLabel)
(import java.awt.Color)
(import java.awt.GridBagLayout)
(import java.awt.GridBagConstraints)
(import javax.swing.SwingUtilities)
;;(import isotscope.uihelpers.DocListener)

(def calc-agent (agent {}))

(defn setup-frame [app]
  (let [cont-pane (.getContentPane app) editor (JTextArea. 5 20)
        results-editor (JTextArea. 5 20)
        back-col Color/BLACK
        edit-col (Color. 20 20 50)
        grid1 (GridBagConstraints.)
        plot-panel (JPanel.)
        ]
  (.setSize app 650 300);;750 500
  (.setVisible app true)
  (.setDefaultCloseOperation app JFrame/EXIT_ON_CLOSE)
  (.setLayout cont-pane (GridBagLayout.))
  (set! (.insets grid1) (java.awt.Insets. 3,3,3,3))
  (set! (.gridwidth grid1) 1)
  (set! (.gridheight grid1) 1)
  (set! (.gridx grid1) 0)
  (set! (.gridy grid1) 1)
  (set! (.fill grid1) GridBagConstraints/BOTH)
  (.add cont-pane editor grid1)
  (.setBackground editor edit-col)
  (.setForeground editor Color/WHITE)
  ;; The uihelpers.DocListener will run
  ;; the on-update callback on updates to the sum formula
  ;; text editor.
  (defn on-update []
    (println "updating now!")
    (let [inp (.getText editor) sf-dct (isotscope.parser/parse-sf-string inp)]
    (println "text is " inp)
    (println "dict is " sf-dct)
    ;;(println "isopat is " (isotscope.isotope/rand-isopat-gen sf-dct 1000))
    (send calc-agent (fn [itm] (isotscope.isotope/rand-isopat-gen sf-dct 1000)))
  ))
  ;; end of the callback for editor updates
  (defn update-loop []
    (Thread/sleep 500)
    (println "<- editor update ->")
    (.setText results-editor (str @calc-agent))
  )
  (.start (Thread. #(while true (update-loop))))
  (.addDocumentListener (.getDocument editor) (isotscope.uihelpers.DocListener. on-update))
  (set! (.gridx grid1) 0)
  (set! (.gridy grid1) 0)
  (.add cont-pane (JLabel. "Sum Formula") grid1)
  (set! (.gridx grid1) 0)
  (set! (.gridy grid1) 3)
  (.add cont-pane (JLabel. "Isotopic Pattern") grid1)
  (set! (.gridx grid1) 0)
  (set! (.gridy grid1) 4)
  (.setBackground results-editor edit-col)
  (.setForeground results-editor Color/WHITE)
  (.add cont-pane results-editor grid1)
  ;; Plot panel
  (.setPreferredSize plot-panel (java.awt.Dimension. 400 200))
  (.setBackground plot-panel Color/RED)
  (set! (.gridx grid1) 1)
  (set! (.gridy grid1) 0)
  (set! (.gridheight grid1) GridBagConstraints/REMAINDER)
  (set! (.anchor grid1) GridBagConstraints/PAGE_START)
  (.add cont-pane plot-panel grid1)
  (.setBackground cont-pane back-col)
  )
  )

(defn ui-main [] (SwingUtilities/invokeAndWait #(let [app (JFrame.)]
                (setup-frame app)
                 app)))











;;
