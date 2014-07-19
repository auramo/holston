;; Used for CSV-import of a very special file-format
;; Example usage:
;; lein run -m holston.dataimport/import-data "/Users/auramo/Downloads/olusia.csv"

(ns holston.dataimport
  (:require [clojure-csv.core :as csv]
            [holston.repository :as repo])
  (:use [clojure.java.io :only [reader]]))

(defn take-csv [file-name]
  (with-open [file (reader file-name)]
    (csv/parse-csv (slurp file))))

(defn parse-row [row]
  {:beer-name (get row 4) :brewery (get row 5)})

(defn parse-tastings [file-name]
  (map parse-row (rest (take-csv file-name))))

;; Adds the brewery if it's not already there
;; Returns id
(defn import-brewery [name]
  (let [brewery-from-db (repo/get-brewery name)]
    (if (> (count brewery-from-db) 0)
      (:id brewery-from-db)
      (do (repo/add-brewery name)
          (:id (repo/get-brewery name))))))

(defn import-named-entity [name search-func add-func]
  (let [record-from-db (search-func name)]
    (if (> (count record-from-db) 0)
      (:id record-from-db)
      (do (add-func name)
          (:id (search-func name))))))

(defn import-one-tasting [raw-tasting]
  (repo/add-tasting {:brewery_id (import-named-entity (:brewery raw-tasting) repo/get-brewery repo/add-brewery)
                     :beer_id (import-named-entity (:beer-name raw-tasting) repo/get-beer repo/add-beer)}))

(defn import-data [file-name]
  (doseq [tasting (parse-tastings file-name)]
    (println "Importing" tasting)
    (import-one-tasting tasting)))
