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
  {:name (get row 4) :brewery (get row 5)})

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

(defn import-one-tasting [raw-tasting]
  (let [brewery-id (import-brewery (:brewery raw-tasting))]
    (repo/add-tasting {:brewery_id brewery-id :name (:name raw-tasting)})))

(defn import-data [file-name]
  (doseq [tasting (parse-tastings file-name)]
    (println "Importing" tasting)
    (import-one-tasting tasting)))
