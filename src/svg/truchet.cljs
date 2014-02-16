(ns svg.truchet
  (:require [reagent.core :as reagent :refer [atom]]))

(def join (partial clojure.string/join " "))

(defn a [start end rad dir]
  (str " M " (join start)
       " A " (join rad) " 0 0 " dir " " (join end)))

(defn tile [x y state]
  (let [s @(:cell-size state)
        half (partial + (/ s 2))
        rad [@(:rad-x state) @(:rad-y state)]
        p1 [x (half y)]
        p2 [(half x) (+ y s)]
        p3 [(half x) y]
        p4[(+ s x) (half y )]]
    (if (> (rand ) 0.5)
      (str (a p1 p3 rad 0)(a p2 p4 rad 1))
      (str (a p1 p2 rad 1)(a p3 p4 rad 0)))))

(defn grid[state]
  (let [w @(:width state)
        h @(:height state)
        size @(:cell-size state)]
    (reduce (fn [r i]
            (let [y (* size (.floor js/Math(/ i w)))
                  x (* size (mod i w))]
              (str r (tile x y state)))) "" (range 0 (* w h)))))

(defn truchet [{:keys [colors state]}]
  (fn [])
  (let [path (grid state)
        colors @colors
        stroke-width @(:stroke-width state)]
      [:svg
       [:g
        (for [i (range (- (count colors) 1) -1 -1)]
          [:path  {:d path
                   :stroke-linecap "round"
                   :fill "none"
                   :stroke (nth colors i)
                   :stroke-width (* stroke-width (/ (+ i 1) 5))}])]]))