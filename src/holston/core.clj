(ns holston.core
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util :refer [format-config-uri]]
            [cheshire.core :as j]
            [clj-http.client :as client]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds]))
  (:use [compojure.route :only [files resources not-found]]
        [compojure.handler :only [site]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        [clojure.java.jdbc :as sql]
        org.httpkit.server)
  (:gen-class))

(def config-auth {:roles #{::user}})

(def client-config
  {:client-id "641379821393-vpt0pi6bdgntd751l2bd5hm1bf7vqrl7.apps.googleusercontent.com"
   :client-secret "f9mMWwhsqhkY9_N1OthLenKO"
   :callback {:domain "http://localhost:8080" :path "/oauth2callback"}})

(def uri-config
  {:authentication-uri {:url "https://accounts.google.com/o/oauth2/auth"
                       :query {:client_id (:client-id client-config)
                               :response_type "code"
                               :redirect_uri (format-config-uri client-config)
                               :scope "https://www.googleapis.com/auth/userinfo.email" }}

   :access-token-uri {:url "https://accounts.google.com/o/oauth2/token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri (format-config-uri client-config)}}})

(defn- fetch-email-address [access-token]
  (let [response (client/get
                  "https://www.googleapis.com/oauth2/v2/userinfo"
                  {:as :json
                   :headers {"Authorization" (str "Bearer " access-token)}})]
  (if (= 200 (:status response))
    (:email (:body response))
    (throw (Exception. "Couldn't retrieve email address")))))

;; Dummy for now
(defn- fetch-roles [email]
  (if (= email "foo.bar@gmail.com")
    #{::admin ::user}
    #{::user}))

(defroutes ring-app
  (GET "/" request "open.")
  (GET "/status" request
       (let [count (:count (:session request) 0)
             session (assoc (:session request) :count (inc count))]
         (-> (ring.util.response/response
              (str "<p>We've hit the session page " (:count session)
                   " times.</p><p>The current session: " session "</p>"))
             (assoc :session session))))
  (GET "/authlink" request
       (friend/authorize #{::user} "Authorized page."))
  (GET "/authlink2" request
       (friend/authorize #{::user} "Authorized page 2."))
  (GET "/logged-in" request
       (str (:cemerick.friend/identity (:session request))))
  (GET "/dlogin" request
       (friend/authenticated "Dummy login page"))
  (GET "/admin" request
       (friend/authorize #{::admin} "Only admins can see this page."))
  (friend/logout (ANY "/logout" request (ring.util.response/redirect "/")))
  (files "/"))

(def app
  (handler/site
   (friend/authenticate
    ring-app
    {:allow-anon? true
     :workflows [(oauth2/workflow
                  {:client-config client-config
                   :uri-config uri-config
                   ;;:config-auth config-auth
                   :auth-error-fn (fn [error]
                                    (ring.util.response/response error))
                   :credential-fn (fn [token]
                                    (let [email (fetch-email-address (:access-token token))]
                                      {:identity token
                                       :email email
                                       :roles (fetch-roles email)}))})]})))

(defn -main
  "Starts the server"
  [port]
  (run-server (site #'app) {:port (Integer. port)}))
