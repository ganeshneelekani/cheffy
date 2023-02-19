(ns cheffy.config
    (:require [integrant.core :as ig]))

(def db {:dbname   (or (System/getenv "DB_DBNAME") "it_data")
         :user     (or (System/getenv "DB_USER") "myuser")
         :password (or (System/getenv "DB_PASSWORD") "mypassword")
         :host     (or (System/getenv "DB_HOST") "localhost")
         :port     (or (System/getenv "DB_PORT") 5432) 
         :dbtype   "postgresql"})

(def migratus-config
  {:store         :database
   :migration-dir "migrations1"
   :db            db})

(def config
  {:server/jetty {:handler (ig/ref :cheffy/app)
                  :port (or (System/getenv "PORT") 3000)}
   :cheffy/app {:jdbc-url (ig/ref :db/postgres)}
   :db/postgres {:jdbc-url db}})