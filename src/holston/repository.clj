;; Contains database-access code

(ns holston.repository
  (:use [clojure.java.jdbc :as jdbc]))

;; If DATABASE_URL environment variable is not set, use 
;; local PostgreSQL database on port 5432 named holston
(def db (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/holston"))

(defn add-tasting [tasting]
  (println tasting)
  (jdbc/insert! db :tasting tasting))

(defn get-brewery [name]
  (first (jdbc/query db ["SELECT id, name FROM brewery WHERE name = ?" name])))

(defn add-brewery [name]
  (jdbc/insert! db :brewery {:name name}))
