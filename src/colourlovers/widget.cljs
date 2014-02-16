(ns colourlovers.widget
  (:require [reagent.core :as reagent :refer [atom]]
            [goog.net.Jsonp] ))


(def colour-lover-url "http://www.colourlovers.com/api/palettes?format=json&keywords=")

(def palette-state (atom "hide"))

(def loading-state (atom "hide"))

(def colors (atom []))

(defn show-palette []
  (if (not-empty @colors)
    (reset! palette-state "")))

(defn hide-palette[]
  (reset! palette-state "hide"))

(defn convert [colors] (map #(str "#" %) colors))

(defn on-success[c]
  (reset! colors c)
  (reset! loading-state "hide")
  (show-palette))


(defn update[value ]
  (reset! palette-state "")
  (reset! loading-state "")
  (reset! colors [])
  (.send (goog.net.Jsonp. (str colour-lover-url value) "jsonCallback") "" on-success))

(defn set-timeout[timeOut callback]
  (js/clearTimeout @timeOut)
  (reset! timeOut (js/setTimeout callback 500 )))


(defn on-change [timeOut event]
  (let [callback (partial update (.. event -target -value))]
    (set-timeout  timeOut callback)))

(defn atom-input []
   (let [timeOut (atom nil)]
   [:input {:type "text"
            :placeholder "colourlovers theme"
            :on-change  (partial on-change timeOut)
            :on-focus show-palette}]))

(defn palette[{:keys [on-select]}]
      [:ul#palette {:className @palette-state}
       [:li {:className (str "spinner " @loading-state)
             :dangerouslySetInnerHTML {:__html "<div class=\"double-bounce1\"/><div class=\"double-bounce2\"/>"}}]

       (for [item @colors]
          [:li.palette
           {:on-click (fn []
                        (hide-palette)
                        (on-select (.. item -colors)))}
           (for [cl (.. item -colors)]
             [:span {:style {:background (str "#" cl)}}])])])


(defn colorpicker [{:keys [s]}]
    [:div
      [:div.style-1.colour-lovers
       [atom-input]
       [palette {:on-select #(reset! s (convert %))}]]])



