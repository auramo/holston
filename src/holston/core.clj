(ns holston.core
  (:use [compojure.route :only [files not-found]]
      [compojure.handler :only [site]]
      [compojure.core :only [defroutes GET POST DELETE ANY context]]
      [clojure.java.jdbc :as sql]
      org.httpkit.server)
  (:gen-class))

(def db-url (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/holston"))

(defn show-landing-page [req]
  (println (str "DB url:" db-url))
  (str "Holston is ready for e-business " (:?column? (first (sql/query db-url
           ["select 'with a DB!'"])))))

(defroutes all-routes
  (GET "/" [] show-landing-page)
  (files "/static/") ;; static file url prefix /static, in `public` folder
  (not-found "<p>Page not found.</p>"))

(defn -main
  "Starts the server"
  [port]
  (run-server (site #'all-routes) {:port (Integer. port)}))
