(ns holston.dataimport
  (:require [clojure-csv.core :as csv])
  (:use [clojure.java.io :only [reader]]))

(defn take-csv
  "Takes file name and reads data."
  [file-name]
  (with-open [file (reader file-name)]
    (csv/parse-csv (slurp file))))

(defn parse-row [row]
  {:name (get row 4) :brewery (get row 5)})

(defn parse-tastings [file-name]
    (map parse-row (take-csv file-name)))
