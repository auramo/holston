(ns holston.restapi
  (:require [holston.repository :as repo]
            [holston.tasting-service :as tasting-service]
            [clojure.data.json :as json])
  (:use [ring.util.response :only [response content-type]]))

(def ok-response {:status "ok"})

(defn- wrap-resp [response-data]
  (content-type (response (json/write-str response-data)) "application/json"))

(defn count-of-tastings []
  (wrap-resp {:count (repo/get-number-of-tastings)}))

(defn strict-get [place key]
  {:pre [(contains? place key)]}
  (get place key))

(defn- convert-tasting [raw-tasting]
  {:beer-name (strict-get raw-tasting "beer")
   :brewery (strict-get raw-tasting "brewery")
   :rating (strict-get raw-tasting "rating")})

(defn- get-email [identity]
  (:email (first (filter #(:email %) (vals (:authentications identity))))))

(defn add-tasting [raw-tasting identity]
   {:pre [identity]}
  (tasting-service/add-tasting (convert-tasting (json/read-str raw-tasting)) (get-email identity))
  (wrap-resp ok-response))

(defn user-info [identity]
  (wrap-resp
   (if identity
     {:status "logged-in" :email (get-email identity)}
     {:status "anonymous"})))

(defn beers []
  (wrap-resp {:beers (repo/get-beers)}))
