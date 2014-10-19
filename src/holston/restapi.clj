(ns holston.restapi
  (:require [holston.repository :as repo]
            [clojure.data.json :as json])
  (:use [ring.util.response :only [response content-type]]))

(def ok-response {:status "ok"})

(defn- wrap-resp [response-data]
  (content-type (response (json/write-str response-data)) "application/json"))

(defn count-of-tastings []
  (wrap-resp {:count (repo/get-number-of-tastings)}))

(defn add-tasting [raw-tasting]
  (println "add tasting" raw-tasting)
  (wrap-resp ok-response))

(defn- get-email [identity]
  (:email (first (filter #(:email %) (vals (:authentications identity))))))

(defn user-info [request]
  (wrap-resp
   (if-let [identity (:cemerick.friend/identity (:session request))]
     {:status "logged-in" :email (get-email identity)}
     {:status "anonymous"})))

(defn beers []
  (wrap-resp {:beers (repo/get-beers)}))
