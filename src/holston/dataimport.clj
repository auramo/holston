;; Used for CSV-import of a very special file-format

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

(defn import-data [file-name]
  (doseq [tasting (parse-tastings file-name)]
    (println "Importing" tasting)
    (repo/add-tasting tasting)))
