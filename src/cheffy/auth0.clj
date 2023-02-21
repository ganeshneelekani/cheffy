(ns cheffy.auth0
  (:require [clj-http.client :as http]
            [muuntaja.core :as m]))

(defn get-test-token
  [email]
  (->> {:content-type :json
        :cookie-policy :standard
        :body (m/encode "application/json"
                        {:client_id "8TvNLScOO4OvaSVae6jYw2NOXV4j9buX"
                         :audience "https://dev-y2udk641.us.auth0.com/api/v2/"
                         :grant_type "password"
                         :username email
                         :password "Kannada123"
                         :scope "openid profile email"})}
       (http/post "https://dev-y2udk641.us.auth0.com/oauth/token")
       (m/decode-response-body)
       :access_token))

(defn get-management-token
  [auth0]
  (->> {:throw-exceptions false
        :content-type :json
        :cookie-policy :standard
        :body (m/encode "application/json"
                        {:client_id "MDi9zHy0rmJxsIF1FeZWVCfHz7pr05LK"
                         :client_secret (:auth0-client-secret auth0)
                         :audience "https://dev-y2udk641.us.auth0.com/api/v2/"
                         :grant_type "client_credentials"})}
       (http/post "https://dev-y2udk641.us.auth0.com/oauth/token")
       (m/decode-response-body)
       :access_token))

(defn get-role-id
  [token]
  (->> {:headers {"Authorization" (str "Bearer " token)}
        :throw-exceptions false
        :content-type :json
        :cookie-policy :standard}
       (http/get "https://dev-y2udk641.us.auth0.com/api/v2/roles")
       (m/decode-response-body)
       (filter (fn [role] (= (:name role) "manage-recipes")))
       (first)
       :id))

(defn create-auth0-user
  [{:keys [connection email password]}]
  (->> {:headers {"Authorization" (str "Bearer " (get-management-token))}
        :throw-exceptions false
        :content-type :json
        :cookie-policy :standard
        :body (m/encode "application/json"
                        {:connection connection
                         :email email
                         :password password})}
       (http/post "https://dev-y2udk641.us.auth0.com/api/v2/users")
       (m/decode-response-body)))


(comment
  
    (let [uid "auth0|63f3527a09b12c77b8d383b6"
          token (get-management-token)]
      (->> {:headers          {"Authorization" (str "Bearer " token)}
            :cookie-policy    :standard
            :content-type     :json
            :throw-exceptions false
            #_#_:body             (m/encode "application/json"
                                            {:roles [(get-role-id token)]})}
           (http/get (str "https://dev-y2udk641.us.auth0.com/api/v2/users/" uid "/roles"))))
  
  (->> {:headers {"Authorization" (str "Bearer " (get-management-token))}
        :throw-exceptions false
        :content-type :json
        :cookie-policy :standard}
       (http/get "https://dev-y2udk641.us.auth0.com/api/v2/roles")
        (m/decode-response-body)
       (filter (fn [role] (= (:name role) "manage-recipes")))
        (first)
       :id
       
       )

  

  (get-test-token))