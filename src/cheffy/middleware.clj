
(ns cheffy.middleware
  (:require [ring.middleware.jwt :as jwt]
            [cheffy.recipe.db :as recipe-db]
             [ring.util.response :as rr]))

(def wrap-auth0
  {:name ::auth0
   :description "Middleware for auth0 authentication and authorization"
   :wrap (fn [handler]
           (jwt/wrap-jwt
            handler
            {:alg :RS256
             :jwk-endpoint "https://dev-y2udk641.us.auth0.com/.well-known/jwks.json"}))})

(def wrap-recipe-owner
  {:name        ::recipe-owner
   :description "Middleware to check if a requestor is a recipe owner"
   :wrap        (fn [handler db]
                  (fn [request]
                   
                    (let [uid (-> request :claims :sub) 
                          recipe-id (-> request :parameters :path :recipe-id) 
                          recipe (recipe-db/find-recipe-by-id db {:uid uid :recipe-id recipe-id})]
                      (if (= (:recipe/uid recipe) uid)
                        (handler request)
                        (-> (rr/response {:message "You need to be the recipe owner"
                                          :data    (str "recipe-id " recipe-id)
                                          :type    :authorization-required})
                            (rr/status 401))))))})

(def wrap-manage-recipes
  {:name ::manage-recipes
   :description "Middlware to check if a user can manage recipes"
   :wrap (fn [handler]
           (fn [request]
             (let [roles (get-in request [:claims "https://dev-y2udk641.us.auth0.com/roles"])]
               (if (some #{"manage-recipes"} roles)
                 (handler request)
                 (-> (rr/response {:message "You need to be a cook to manager recipes"
                                   :data    (:uri request)
                                   :type    :authorization-required})
                     (rr/status 401))))))})