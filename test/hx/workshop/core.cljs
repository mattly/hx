(ns hx.workshop.core
  (:require [devcards.core :as dc :include-macros true]
            [hx.react :as react :include-macros true]))

(dc/defcard
  macroexpand
  (macroexpand '(react/compile
                $[:div {:style {:color "green"}
                        :id "asdf"} "hello"])))

(dc/defcard
  simple
  (react/compile
   $[:div {:style {:color "green"}
          :id "asdf"} "hello"]))

(dc/defcard
  with-children
  (react/compile
   $[:ul {:style {:background "lightgrey"}}
     [:li {:style {:font-weight "bold"}} "one"]
     [:li "two"]
     [:li "three"]]))

(dc/defcard
  conditional
  (react/compile
   $[:<>
     (when true
       $[:div "true"])
     (when false
       $[:div "false"])]))

(dc/defcard
  seq
  (react/compile
   $[:ul
     (list $[:li {:key 1} 1]
           $[:li {:key 2} 2])]))

(dc/defcard
  map
  (react/compile
   (let [numbers [1 2 3 4 5]]
     $[:ul {:style {:list-style-type "square"}}
       (map #(do $[:li {:key %} %])
            numbers)])))

(dc/defcard css-class
  (react/compile
   $[:<>
     [:style {:dangerouslySetInnerHTML #js {:__html ".foo { color: lightblue }"}}]
     [:div {:className "foo"} "asdf jkl"]
     [:div {:class "foo"} "1234 bnm,"]]))

(dc/defcard defnc
  (macroexpand '(react/defnc greeting [{:keys [name] :as props}]
                  (println props)
                  $[:span {:style {:font-size "24px"}}
                    "Hello, " name])))

(react/defnc greeting [{:keys [name] :as props}]
  $[:span {:style {:font-size "24px"}}
    "Hello, " name])

(dc/defcard
  function-element
  (react/compile
   $[greeting {:name "Will"}]))

(react/defnc with-children [{:keys [children]}]
  $[:div
    (identity children)])

(dc/defcard with-children
  (react/compile
   $[with-children
     [:span "hi"]
     [:div "watup"]]))

(dc/defcard defcomponent
  (macroexpand '(react/defcomponent some-component
                  (constructor [this]
                               this)
                  (render [this]
                          $[:div "sup component"]))))

(react/defcomponent
  some-component
  (constructor [this]
               this)
  (render [this]
          $[:div "sup component"]))

(dc/defcard class-element
  (react/compile
   $[some-component]))

(react/defcomponent stateful
  (constructor [this]
               (set! (.. this -state) #js {:name "Will"})
               this)
  (update-name! [this e]
                (. this setState #js {:name (.. e -target -value)}))
  (render [this]
          (let [state (. this -state)]
            $[:div
              [:div (. state -name)]
              [:input {:value (. state -name)
                       :on-change (. this -update-name!)}]])))

(dc/defcard stateful-element
  (react/compile
   $[stateful]))

(react/defcomponent static-property
  (constructor [this]
               this)

  ^:static
  (some-prop "1234")

  (render [this]
          $[:div (. static-property -some-prop)]))

(dc/defcard stateful-element
  (react/compile
   $[static-property]))

(react/defcomponent fn-as-child
  (constructor [this]
               (set! (. this -state) #js {:name "Will"})
               this)
  (update-name! [this e]
                (. this setState #js {:name (.. e -target -value)}))
  (render [this]
          (let [state (. this -state)]
            $[:div
              [:div ((.. this -props -children) (. state -name))]
              [:input {:value (. state -name)
                       :on-change (. this -update-name!)}]])))

(dc/defcard fn-as-child
  (react/compile
   $[fn-as-child
     (fn [name]
       $[:span {:style {:color "red"}} name])]))

(react/defcomponent render-prop
  (constructor [this]
               (set! (. this -state) #js {:name "Will"})
               this)
  (update-name! [this e]
                (. this setState #js {:name (.. e -target -value)}))
  (render [this]
          (let [state (. this -state)]
            $[:div
              [:div ((.. this -props -render) (. state -name))]
              [:input {:value (. state -name)
                       :on-change (. this -update-name!)}]])))

(dc/defcard render-prop
  (react/compile
   $[render-prop
     {:render (fn [name]
                $[:span {:style {:color "red"}} name])}]))

(def js-interop-test
  (fn
    [props]
    (js/console.log props)
    "blahblah"))

(js/console.log js-interop-test)

(dc/defcard js-interop-nested-props
  (react/compile
   $[js-interop-test {:nested {:thing {:foo {:bar "baz"}}}}]))

(defn ^:dev/after-load start! []
  (dc/start-devcard-ui!))

(defn init! [] (start!))

(init!)
