(ns holston.tasting-service
  (:require [holston.repository :as repo]))

;; Adds a record if it's not there already, otherwise doesn't do any modifications
;; Returns the id of the record
(defn- import-named-entity [name search-func add-func]
  (let [record-from-db (search-func name)]
    (if (> (count record-from-db) 0)
      (:id record-from-db)
      (do (add-func)
          (:id (search-func name))))))

(defn- add-beer [beer-name brewery-name beer-style]
  (fn []
    (repo/add-beer
     beer-name
     (import-named-entity brewery-name repo/get-brewery (fn [] (repo/add-brewery brewery-name)))
     (import-named-entity beer-style repo/get-beer-style (fn [] (repo/add-beer-style beer-style))))))

(defn- add-or-select-beer [beer-name brewery-name beer-style]
  (import-named-entity beer-name repo/get-beer (add-beer beer-name brewery-name beer-style)))

(defn add-tasting [raw-tasting user-id]
  (repo/add-tasting 
   {:beer_id (add-or-select-beer (:beer-name raw-tasting) (:brewery raw-tasting) (:beer-style raw-tasting))
    :user_rating (:rating raw-tasting)
    :user_id user-id}))

