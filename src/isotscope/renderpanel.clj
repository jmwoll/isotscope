;; Copyright (c) 2017 Jan Wollschläger
;;
;; This program and the accompanying materials are made
;; available under the terms of the Eclipse Public License 2.0
;; which is available at https://www.eclipse.org/legal/epl-2.0/
;;
;; SPDX-License-Identifier: EPL-2.0

(ns isotscope.renderpanel
  ;;(:require [javax.swing.JPanel :refer :all])
  (:gen-class
    ;; :name com.isot.DocListener
    :init init
    :name isotscope.renderpanel.RenderPanel
    :extends javax.swing.JPanel
    :constructors {[Object] []}
    :state state
    )
  )

(import javax.swing.JPanel)
(import java.awt.Color)
(import java.awt.geom.Line2D)

(defn -init [state]
  (println "constructing renderpanel")
  [[] (atom state)]
  )

(defn get-data [this]
  (let [state (.state this)]
  (deref @state)
  ))

(defn height [this]
  (.getHeight this)
  )

(defn width [this]
  (.getWidth this)
  )

(defn fill-background [this g]
  (.fillRect g 0 0 (width this) (height this))
  )

(defn draw-line [this g fx fy sx sy]
  (.draw g (java.awt.geom.Line2D$Double. fx (- (height this) fy) sx (- (height this) sy)))
  )


(defn draw-string [this g text x y]
  (.drawString g text (int x) (- (height this) (int y)))
  )


(defn centered-string [this g text x y]
  (let [font (.getFont (javax.swing.JLabel.))
        fm (.getFontMetrics g)
        strw (javax.swing.SwingUtilities/computeStringWidth fm text)
    ]
      (draw-string this g text (- x (/ strw 2)) y)
      )
      ;;(let [metrics (.getFontMetrics g (.getFont g))]
          ;;(.drawString g text (- x (.stringWidth metrics text)) y)
        ;;(.drawString g text x (- (height this) y))
      ;;)
  )


(defn -paintComponent [this g]
  (let [state (.state this)
        fg (Color. 180 180 220)
        bg (Color. 0 0 0)
        b-strk (java.awt.BasicStroke. 0.4 java.awt.BasicStroke/CAP_ROUND
                java.awt.BasicStroke/JOIN_MITER)
        peak-col (Color. 80 180 80)
        peak-strk (java.awt.BasicStroke. 2.5 java.awt.BasicStroke/CAP_ROUND
                java.awt.BasicStroke/JOIN_MITER)
        ypad 22 xpad 20
        xplotpad 10
        data (get-data this)
        ]
        (.setColor g bg)
        (fill-background this g)
        (.setColor g fg)
        (.setStroke g b-strk)
        (draw-line this g xpad ypad (- (width this) xpad) ypad)
        ;; x axis must be mapped to range (0,width) but with padding!
        ;; therefore xpad + (width - 2*xpad) * (x - min) / (max - min)
        ;;(defn y-norm [y ymin ymax] (float (+ ypad (* (- (height this) (* 2 ypad)) (/ (- y ymin) (- ymax ymin))))))
        (defn y-norm [y ymin ymax] (float (* (/ y ymax) (height this))))
        (defn x-norm [x dmin dmax] (float (+ xpad (* (- (width this) (* 2 xpad)) (/ (- x dmin) (max 1 (- dmax dmin)))))))
        (when (not (empty? (filter (fn [x] (not (zero? x))) (keys data))))
          (let [dmin (apply min (keys data)) dmax (apply max (keys data))
                ymin (apply min (vals data)) ymax (apply max (vals data))
                data-screen (map (fn [xy] [(x-norm (first xy) dmin dmax)
                                           (y-norm (second xy) ymin ymax)]) data)
               ]
            ;;(println data-screen)
            (.setStroke g peak-strk)
            (.setColor g peak-col)
            (dorun (map (fn [itm] (let [xy (first itm) orgxy (second itm) x (first xy) y (second xy)]
                            (assert (< 0 y))
                           (let [x (/ (+ x xpad) (/ (+ (width this) (* 2 xpad)) (width this)))
                                 y (+ ypad (/ y (/ (+ (height this) (* 1 ypad)) (height this))))
                           ]
                             (assert (<= ypad y))
                             (draw-line this g x ypad x y)
                             ;; only mz i.e. (first orgxy) or also intensity
                             ;; aka (second orgxy) ??
                             (when (> 10 (count data))
                                (centered-string this g (str (first orgxy)) x 0)
                             )
                           )
                           )) (partition 2 (interleave data-screen data))))
            ;;(dorun (map (fn [itm] (println "itm")) data-screen))
            )
        )
    )
  )
