;; Contains database-access code

(ns holston.repository
  (:use [clojure.java.jdbc :as jdbc]))

;; If DATABASE_URL environment variable is not set, use 
;; local PostgreSQL database on port 5432 named holston
(def db (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/holston"))

(defn add-tasting [tasting]
  (jdbc/insert! db :tasting tasting))

(defn get-number-of-tastings []
  (:count (first (jdbc/query db ["select count(*) from tasting"]))))

(defn get-brewery [name]
  (first (jdbc/query db ["SELECT id, name FROM brewery WHERE name = ?" name])))

(defn add-brewery [name]
  (jdbc/insert! db :brewery {:name name}))

(defn get-beer [name]
  (first (jdbc/query db ["SELECT id, name FROM beer WHERE name = ?" name])))

(defn get-beers []
  (jdbc/query db ["SELECT id, name FROM beer"]))

(defn add-beer [name]
  (jdbc/insert! db :beer {:name name}))

(defn db-sanity-check []
  (str "Static query from DB value: " (:?column? (first (jdbc/query db
           ["select 'foobar'"])))))
