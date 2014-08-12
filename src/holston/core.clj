(ns holston.core
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            [holston.auth :as auth]
            [holston.restapi :as api])
  (:use [compojure.route :only [files resources not-found]]
        [compojure.handler :only [site]]
        [ring.util.response :only [response]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]
        org.httpkit.server)
  (:gen-class))

(defroutes ring-app
  (GET "/" request "open.")

  ;; API methods
  (GET "/api/tastings/count" request (api/count-of-tastings))

  ;; Dummy stuff which "tests" now authorization, to be removed
  (GET "/authlink" request
       (friend/authorize #{::user} "Authorized page."))
  (GET "/authlink2" request
       (friend/authorize #{::user} "Authorized page 2."))
  (GET "/admin" request
       (friend/authorize #{::admin} "Only admins can see this page."))

  ;; Authentication stuff
  (GET "/logged-in" request
       (str (:cemerick.friend/identity (:session request))))
  (GET "/dlogin" request
       (friend/authenticated "Dummy login page"))
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
