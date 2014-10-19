;; Used for CSV-import of a very special file-format
;; Example usage:
;; lein run -m holston.dataimport/import-data "/Users/auramo/Downloads/olusia.csv"

(ns holston.dataimport
  (:require [clojure-csv.core :as csv]
            [holston.tasting-service :as tasting-service])
  (:use [clojure.java.io :only [reader]]))

(defn take-csv [file-name]
  (with-open [file (reader file-name)]
    (csv/parse-csv (slurp file))))

(defn parse-int [str]
  (try (Integer/parseInt str) (catch Exception e nil)))

(defn parse-row [row]
  {:beer-name (get row 4) :brewery (get row 5) :rating (parse-int (get row 11))})

(defn parse-tastings [file-name]
  (map parse-row (rest (take-csv file-name))))

(defn import-data [file-name]
  (doseq [tasting (parse-tastings file-name)]
    (println "Importing" tasting)
    (tasting-service/add-tasting tasting)))
