;; Copyright (c) 2018 Jan Wollschläger
;;
;; This program and the accompanying materials are made
;; available under the terms of the Eclipse Public License 2.0
;; which is available at https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0


(ns isotscope.ui
  (:require [isotscope.uihelpers :refer :all])
  (:require [isotscope.parser :refer :all])
  (:require [isotscope.isotope :refer :all])
  (:require [isotscope.renderpanel :refer :all])
  )
(import javax.swing.JFrame)
(import javax.swing.JPanel)
(import javax.swing.JTextArea)
(import javax.swing.JLabel)
(import javax.swing.JScrollPane)
(import javax.swing.SwingUtilities)
(import java.awt.Color)
(import java.awt.GridBagLayout)
(import java.awt.GridBagConstraints)
;;(import isotscope.uihelpers.DocListener)

(def calc-agent (agent {}))
(def previous-text (agent "nil"))
(def charge-agent (agent 1))

;;(def testatom (atom "test"))

(defn setup-frame [app]
  (let [cont-pane (.getContentPane app) editor (JTextArea. 5 20)
        results-editor (JTextArea. 5 20)
        back-col Color/BLACK
        edit-col (Color. 20 20 50)
        grid1 (GridBagConstraints.)
        plot-panel (isotscope.renderpanel.RenderPanel. {:calc calc-agent :charge charge-agent})
        ;;plot-panel (JPanel.)
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
  (.add cont-pane (JScrollPane. editor) grid1)
  (.setBackground editor edit-col)
  (.setForeground editor Color/WHITE)
  (.setCaretColor editor Color/RED)

  (defn updater []
    (Thread/sleep 500)
    (let [inp (.getText editor)
          sf-dct (try (isotscope.parser/parse-sf-string inp) (catch Exception e {}))
          _ (println "sum formula:" sf-dct)
          ;;pos-key (keyword "+") neg-key (keyword "-")
          pos-charge (get sf-dct :+) neg-charge (get sf-dct :-)
          pos-charge (if (= pos-charge nil) 0 pos-charge)
          neg-charge (if (= neg-charge nil) 0 neg-charge)
          total-charge (- pos-charge neg-charge)
          sf-dct (dissoc (dissoc sf-dct :+) :-)
            ]
    ;; -- handle charges
    (send charge-agent (fn [itm] total-charge))
    ;;(println "isopat is " (isotscope.isotope/rand-isopat-gen sf-dct 1000))
    ;;(send calc-agent (fn [itm] (isotscope.isotope/rand-isopat-gen sf-dct 1000)))
    (let [pt @previous-text]
      (if (not= pt inp) (send calc-agent (fn [itm] {}))))

    (send calc-agent
      (fn [itm] (isotscope.parser/add-sf-dicts itm
        (try (isotscope.isotope/rand-isopat-gen sf-dct 1000)
        (catch Exception e itm)
        ))))
    (send previous-text (fn [itm] inp))
    ;; this old code works, but will not work after adding
    ;; the :charge attribute to the state of the render-panel
    ;; (.setText results-editor (isotscope.parser/pretty-print-sf @calc-agent))
    (.setText results-editor (isotscope.parser/pretty-print-sf (get-data plot-panel)))

    ;; also update plot-panel
    (.repaint plot-panel)
  ))
  ;;
  (defn update-loop []
    (while true (updater)))
  ;;
  (.start (Thread. update-loop))
  ;;(.addDocumentListener (.getDocument editor) (isotscope.uihelpers.DocListener. on-update))
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
  (.add cont-pane (JScrollPane. results-editor) grid1)
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
