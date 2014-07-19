;; Contains database-access code

(ns holston.repository
  (:use [clojure.java.jdbc :as jdbc]))

;; If DATABASE_URL environment variable is not set, use 
;; local PostgreSQL database on port 5432 named holston
(def db (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/holston"))

(defn add-tasting [tasting]
  (jdbc/insert! db :tasting tasting))

