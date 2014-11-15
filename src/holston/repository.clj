;; Contains database-access code, very plain. No higher-order logic here.

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

(defn get-beer-style [name]
  (first (jdbc/query db ["SELECT id, name FROM beer_style WHERE name = ?" name])))

(defn add-beer-style [name]
  (jdbc/insert! db :beer_style {:name name}))

(defn get-beer [name]
  (first (jdbc/query db ["SELECT id, name FROM beer WHERE name = ?" name])))

(defn get-beers []
  (jdbc/query db ["SELECT id, name FROM beer"]))

(defn get-beer-styles []
  (jdbc/query db ["SELECT id, name FROM beer_style"]))

(defn get-breweries []
  (jdbc/query db ["SELECT id, name FROM brewery"]))

(defn get-beer-ratings []
  (jdbc/query db ["SELECT avg(t.user_rating) as average_rating, count(*) as rating_amount, b.name as beer_name
                   FROM tasting t, beer b 
                   WHERE t.beer_id = b.id group by t.beer_id, beer_name"]))

(defn add-beer [name brewery-id beer-style-id alcohol]
  (jdbc/insert! db :beer {:name name :brewery_id brewery-id :beer_style_id beer-style-id :alcohol alcohol}))

(defn db-sanity-check []
  (str "Static query from DB value: " (:?column? (first (jdbc/query db
           ["select 'foobar'"])))))
