(ns user
  (:require [cheffy.config :as a]
            [cheffy.server]
            [integrant.repl :as ig-repl]
            [integrant.repl.state :as state]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [reitit.coercion]
            [reitit.coercion.spec]
            [reitit.core]))

(ig-repl/set-prep!
 (fn []
   a/config))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> state/system :cheffy/app))
(def db (-> state/system :db/postgres))

(comment
  (go)
  
  (halt)
  
  (reset)
  
  (app {:request-method :get
        :uri            "/swagger.json"})
  
  (app {:request-method :get
        :uri            "/v1/recipes/1234-recipe"})
  
    (-> (app {:request-method :get
              :uri "/v1/recipes/1234-recipe"})
        :body
        (slurp))
  
  (-> (app {:request-method :post
            :uri "/v1/recipes"
            :body-params {:name "my recipe"
                          :prep-time 49
                          :img "image-url"}})
      :body
      (slurp))
  

  
  (jdbc/execute! db ["SELECT * FROM recipe WHERE public = true"])
  
  (sql/find-by-keys db :recipe {:public true})

  (time
   (with-open [conn (jdbc/get-connection db)]
     {:public (sql/find-by-keys conn :recipe {:public true})
      :drafts (sql/find-by-keys conn :recipe {:public false
                                              :uid    "auth0|5ef440986e8fbb001355fd9c"})}))
  
  (with-open [conn (jdbc/get-connection db)]
    (let [recipe-id   "a3dde84c-4a33-45aa-b0f3-4bf9ac997680"
          [recipe]    (sql/find-by-keys conn :recipe {:recipe_id recipe-id})
          steps       (sql/find-by-keys conn :step {:recipe_id recipe-id})
          ingredeints (sql/find-by-keys conn :ingredient {:recipe_id recipe-id})]
      (when (seq recipe)
        (assoc recipe
               :recipe/steps steps
               :recipe/ingredients ingredeints)))) 
  
  

  (app {:request-method :get
        :uri            "/v1/recipes/1234"})
  
  (app {:request-method :get
        :uri            "/v1/recipes"})
  
  (app {:request-method :get
        :uri            "/"})
  
  (app {:request-method :post
        :uri            "/v1/recipes"
        :body-params    {:name      "my recipe"
                         :prep-time 4
                         :img       "image-url"
                         :public    false}}) 


  (rrouter/match-by-path router "/v1/recipes/12
                                 34-recipe")
  
  (jdbc/execute! db ["select * from recipe where public = true"])

  (sql/find-by-keys db :recipe {:public true})

  
  (reset-all)
  
  )
