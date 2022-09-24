(ns cheffy.server
  (:require [cheffy.router :as r]
            [environ.core :refer [env]]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(defn app
  [env]
  (println "----EE----" env)
  (r/routes env))

(defmethod ig/prep-key :server/jetty
  [_ config]
  (merge config {:port (Integer/parseInt (env :port))}))

(defmethod ig/prep-key :db/postgres
  [_ config]
  (merge config {:jdbc-url (env :jdbc-url)}))

(defmethod ig/init-key :server/jetty
  [_ {:keys [handler port]}]
  (jetty/run-jetty handler {:port  port
                            :join? false})
  (println " Server running on port " port))

(defmethod ig/init-key :cheffy/app
  [_ config]
  (println " Started app")
  (app config))

(defmethod ig/init-key :db/postgres
  [_ config]
  (println " Configured db")
  (:jdbc config))

(defmethod ig/halt-key! :server/jetty
  [_ jetty]
  (.stop jetty))

(defn -main
  "I don't do a whole lot ... yet."
  [config-file]
  (println "---1---")
  (let [config (-> config-file slurp ig/read-string)]
    (-> config ig/prep ig/init)))

(comment
  (app {:request-method :get
        :uri            "/"})
  (-main))

