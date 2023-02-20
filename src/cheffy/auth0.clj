(ns cheffy.auth0
  (:require [clj-http.client :as http]
            [muuntaja.core :as m]))

(defn get-test-token
  []
  (->> {:content-type :json
        :cookie-policy :standard
        :body (m/encode "application/json"
                        {:client_id "8TvNLScOO4OvaSVae6jYw2NOXV4j9buX"
                         :audience "https://dev-y2udk641.us.auth0.com/api/v2/"
                         :grant_type "password"
                         :username "testing@cheffy.app"
                         :password "Kannada123"
                         :scope "openid profile email"})}
       (http/post "https://dev-y2udk641.us.auth0.com/oauth/token")
       (m/decode-response-body)
       :access_token))

(comment
  (get-test-token))