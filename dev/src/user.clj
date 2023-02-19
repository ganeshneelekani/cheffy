(ns user
  (:require [cheffy.server] 
            [cheffy.config :as a] 
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [reitit.core :as reitit]
            [reitit.coercion :as coer]
            [integrant.repl :as ig-repl]
            ;;[reitit.core :as rrouter]
            [integrant.repl.state :as state]
            [reitit.coercion.spec :as rspec]))

(ig-repl/set-prep!
 (fn []
   a/config))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> state/system :cheffy/app))
(def db (-> state/system :db/postgres))

(def router
  (reitit.core/router
   ["/v1/recipes/:recipe-id"
    {:coercion rspec/coercion
     :parameters {:path {:recipe-id int?}}}]
   {:compile coer/compile-request-coercers}))

(comment
  (go)
  (coer/coerce!
   (rrouter/match-by-path router "/v1/recipes/1234")
   )
  (app {:request-method :get
        :uri "/v1/recipes/1234"})
  
    (app {:request-method :get
          :uri "/v1/recipes"})
  
  (app {:request-method :get
        :uri "/"})
  
    (app {:request-method :post
          :uri "/v1/recipes"
          :body-params {:name "my recipe"
                         :prep-time 4
                         :img "image-url"
                         :public false}})
  
  (:require  '[clojure.pprint :refer [pprint]])
  (rrouter/match-by-path router "/v1/recipes/1234-recipe")
  
  (jdbc/execute! db ["select * from recipe where public = true"])

  (sql/find-by-keys db :recipe {:public true})
  (halt)
  (reset)
  
  (reset-all)
  
  )
