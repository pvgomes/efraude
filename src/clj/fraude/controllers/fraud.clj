(ns fraude.controllers.fraud
  (:require
    [fraude.db.core :as db]))

(defn save! [{:keys [name message caution fk_person url]}]
  (db/create-fraud! {:name name
                     :message message
                     :caution caution
                     :fk_person fk_person
                     :url url}))

(defn get-fraud [id]
  (db/get-fraud id))

(defn dashboard []
  {:last (db/get-frauds {:fraud_limit 5})
   :top (db/get-frauds-asc {:fraud_limit 5})})