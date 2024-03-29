(ns cheffy.server
  (:require [cheffy.config :as a]
            [ring.adapter.jetty :as jetty]
            [integrant.core :as ig]
            [migratus.core :as m]
            [next.jdbc :as jdbc]
            [environ.core :refer [env]]
            [cheffy.router :as router]))

(defn app
  [env]
  (router/routes env))

(defmethod ig/prep-key :server/jetty
  [_ config]
  config)

(defmethod ig/prep-key :auth/auth0
  [_ config]
  (merge config {:client-secret (env :auth0-client-secret)}))

(defmethod ig/init-key :server/jetty
  [_ {:keys [handler port]}]
  (println (str "\nServer running on port " port))
  (jetty/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key :cheffy/app
  [_ config]
  (println "\nStarted app")
  (app config))

(defmethod ig/init-key :db/postgres
  [_ {:keys [jdbc-url]}]
  (println "\nConfigured db")
  (jdbc/with-options jdbc-url jdbc/snake-kebab-opts))

(defmethod ig/init-key :auth/auth0
  [_ auth0]
  (println "\nConfigured auth0")
  auth0)

(defmethod ig/halt-key! :server/jetty
  [_ jetty]
  (.stop jetty))

(defn -main
  []
  (let [system  (-> a/config ig/prep ig/init)]
  ;; (migrate-sql-statements) If you are not inserting data while docker srart up
    system))

(comment
  (start)
  (app {:request-method :get
        :uri "/"})
  (-main))
