(ns app
  (:require [reagent.core :as reagent :refer [atom]]
            [colourlovers.widget]
            [svg.truchet]))

(def label {:cell-size "Cell Size"
            :width "Width"
            :stroke-width "Line Width"
            :height "Height"
            :rad-x "rad x"
            :rad-y "rad-y"})


(defn widget[]

  (let[selected (atom ["#A1DBB2" "#FEE5AD" "#FACA66" "#F7A541" "#F45D4C"])
       state {:cell-size (atom 100)
              :stroke-width (atom 10)
              :width (atom 10)
              :height (atom 10)
              :rad-x (atom 50)
              :rad-y (atom 50)}]
    (fn []
    [:div {:className "app"}
     [:div {:className "settings"}
      [colourlovers.widget.colorpicker {:s selected}]
      (for [[k v ] state]
        [:p
          [:label (k label)]
          [:input {:type "range"
                   :on-change #(reset! v (js/parseInt (.. % -target -value)))}]])]

      [svg.truchet.truchet {:colors selected :state state}]])))

(reagent/render-component [widget]
  (.-body js/document))