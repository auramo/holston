(ns holston.tasting-service
  (:require [holston.repository :as repo]))

;; Adds a record if it's not there already, otherwise doesn't do any modifications
;; Returns the id of the record
(defn- import-named-entity [name search-func add-func]
  (let [record-from-db (search-func name)]
    (if (> (count record-from-db) 0)
      (:id record-from-db)
      (do (add-func name)
          (:id (search-func name))))))

(defn add-tasting [raw-tasting user-id]
  (println (str "user email in add tasting " user-id)) ;; TODO add column and add field to repository
  (repo/add-tasting {:brewery_id (import-named-entity (:brewery raw-tasting) repo/get-brewery repo/add-brewery)
                     :beer_id (import-named-entity (:beer-name raw-tasting) repo/get-beer repo/add-beer)
                     :user_rating (:rating raw-tasting)
                     :user_id user-id}))

