(ns blocks.core
    (:require [reagent.core :as reagent :refer [atom]]
              [blocks.util :refer [blockify]]))

(enable-console-print!)

(def source-code (atom ""))
(def html-out (atom ""))


(defn editor-did-mount []
  (fn [this]
    (let
      [cm (.fromTextArea js/CodeMirror
            (reagent/dom-node this)
            #js {:mode "clojure"
                 :lineNumbers true
                 :tabSize 2})]
      (.setValue cm ";; Type some Clojure code here\n")
      (.on cm "change" #(reset! source-code (.getValue %))))))

(defn code-pane []
  (reagent/create-class
    {:render (fn [] [:textarea#editor])
     :component-did-mount (editor-did-mount)}))

(defn make-blocky []
  (reset! html-out 
    (str "<div id='code'>" (blockify @source-code) "</div>")))

(defn blocks-pane []
  [:div#blocks-pane
    [:div#block-button [:button {:on-click make-blocky} "Make it blocky!"]]
    [:div#blocks {:dangerouslySetInnerHTML {:__html @html-out}}]])

(defn app []
  [:div#container
    [:div#code-pane [code-pane]]
    [blocks-pane]])

(reagent/render-component [app]
  (. js/document (getElementById "app")))
