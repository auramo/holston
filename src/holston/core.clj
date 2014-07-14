(ns holston.core
  (:use [compojure.route :only [files not-found]]
      [compojure.handler :only [site]]
      [compojure.core :only [defroutes GET POST DELETE ANY context]]
      org.httpkit.server))

(defn show-landing-page [req]
  "Holston ready for e-business")

(defroutes all-routes
  (GET "/" [] show-landing-page)
  (files "/static/") ;; static file url prefix /static, in `public` folder
  (not-found "<p>Page not found.</p>"))

(defn -main
  "Starts the server"
  [port]
  (run-server (site #'all-routes) {:port (Integer. port)}))
