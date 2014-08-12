(ns holston.core
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            [cheshire.core :as j]
            [holston.auth :as auth]
            [holston.repository :as repo])
  (:use [compojure.route :only [files resources not-found]]
        [compojure.handler :only [site]]
        [ring.util.response :only [response]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        org.httpkit.server)
  (:gen-class))

(defroutes ring-app
  (GET "/" request "open.")
  (GET "/api/tastings/count" request (response (str (repo/get-number-of-tastings))) )
  (GET "/status" request
       (let [count (:count (:session request) 0)
             session (assoc (:session request) :count (inc count))]
         (-> (response
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
    auth/auth-params)))

(defn -main
  "Starts the server"
  [port]
  (println "Starting Holston")
  (run-server (site #'app) {:port (Integer. port)}))
