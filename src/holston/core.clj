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
  (GET "/api/beers" request (api/beers))
  (GET "/api/breweries" request (api/breweries))
  (POST "/api/tastings" request (api/add-tasting (slurp (request :body)) (:cemerick.friend/identity (:session request))))
  (GET "/api/ratings" request (api/get-beer-ratings))
  (GET "/api/userInfo" request (api/user-info (:cemerick.friend/identity (:session request))))

  ;; Auth stuff
  (GET "/auth/login" {params :query-params}
       (friend/authenticated (ring.util.response/redirect (str "/index.html#" (params "path")))))
  (friend/logout (ANY "/auth/logout" request (ring.util.response/redirect "/")))

  ;; Dummy stuff which "tests" now authorization, to be removed
  (GET "/authlink" request
       (friend/authorize #{::user} "Authorized page."))
  (GET "/admin" request
       (friend/authorize #{::admin} "Only admins can see this page."))

  
  (files "/")
  (compojure.route/not-found "404 not found"))

(def app
  (handler/site
   (friend/authenticate
    ring-app
    auth/auth-params)))

(defn start-server [port]
  (println (str "Starting Holston on port " port))
  (run-server (site #'app) {:port port})) 

(defn -main
  "Starts the server"
  [port]
  (start-server (Integer. port)))
