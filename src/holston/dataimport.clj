(ns holston.dataimport
  (:require [clojure-csv.core :as csv]))

(defn take-csv
  "Takes file name and reads data."
  [fname]
  (with-open [file (clojure.java.io/reader fname)]
    (csv/parse-csv (slurp file))))
