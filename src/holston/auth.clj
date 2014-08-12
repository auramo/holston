(ns holston.auth
  (:require [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util :refer [format-config-uri]]
            [clj-http.client :as client]))

(defn- get-mandatory-env-var [name]
  (or (System/getenv name)
      (slurp (str (System/getenv "ENV_DIR") "/" name)) ;; This happens in macroexpansion and heroku only reveals config vars this way in compile
      (throw (Exception. (str "Mandatory environment variable missing: " name)))))

(defn- get-oauth-callback-domain []
  (if (or (System/getenv "ENV_DIR") (System/getenv "DYNO")) ;; We're in Heroku, not local env
    (get-mandatory-env-var "HOLSTON_OATH2_CALLBACK_DOMAIN")
    "http://localhost:8080"))

(defn- create-client-config []
  {:client-id (get-mandatory-env-var "HOLSTON_GOOGLE_CLIENT_ID")
   :client-secret (get-mandatory-env-var "HOLSTON_GOOGLE_CLIENT_SECRET")
   :callback {:domain (get-oauth-callback-domain) :path "/oauth2callback"}})

(defn- uri-config []
  (let [client-config (create-client-config)]
    {:authentication-uri {:url "https://accounts.google.com/o/oauth2/auth"
                          :query {:client_id (:client-id client-config)
                                  :response_type "code"
                                  :redirect_uri (format-config-uri client-config)
                                  :scope "https://www.googleapis.com/auth/userinfo.email" }}

     :access-token-uri {:url "https://accounts.google.com/o/oauth2/token"
                        :query {:client_id (:client-id client-config)
                                :client_secret (:client-secret client-config)
                                :grant_type "authorization_code"
                                :redirect_uri (format-config-uri client-config)}}}))

(defn- fetch-email-address [access-token]
  (let [response (client/get
                  "https://www.googleapis.com/oauth2/v2/userinfo"
                  {:as :json
                   :headers {"Authorization" (str "Bearer " access-token)}})]
  (if (= 200 (:status response))
    (:email (:body response))
    (throw (Exception. "Couldn't retrieve email address")))))

(defn- fetch-roles [email]
  (if (= email "foo.bar@gmail.com")
    #{::admin ::user}
    #{::user}))

(def auth-params {:allow-anon? true
     :workflows [(oauth2/workflow
                  {:client-config (create-client-config)
                   :uri-config (uri-config)
                   :auth-error-fn (fn [error]
                                    (ring.util.response/response error))
                   :credential-fn (fn [token]
                                    (let [email (fetch-email-address (:access-token token))]
                                      {:identity token
                                       :email email
                                       :roles (fetch-roles email)}))})]})
