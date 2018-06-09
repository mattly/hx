(ns hx.compiler.generator
  (:require [clojure.walk :as walk]
            [hx.utils :as utils]))

(defn analyze-props [props]
  ;; optimize case when map literal is passed in for props
  (if (map? props)
    (utils/shallow-clj->js props)
    props))

(defn element? [leaf]
  (:hx/analyzed leaf))

(declare generate-children)

(defn make-node [create-element {:keys [el props children]}]
  `(~create-element
          ~el
          ~(analyze-props props)
          ~@(into [] (generate-children create-element children))))

(defn generate-element [create-element leaf]
  (if (element? leaf)
    (make-node create-element leaf)
    leaf))

(defn generate-children [create-element children]
  (map (partial generate-element create-element) children))

(defn generate [tree create-element]
  (walk/prewalk
   (partial generate-element create-element)
   tree))

#_(-> [:div
       [:span "wat"]
       ['widget]
       [:input {:type "button"} [:i {:id "jkl"} "sup"]]]
      (hx.compiler.parser/parse)
      (hx.compiler.analyzer/analyze)
      (generate 'react/createElement)
      )