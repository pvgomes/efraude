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
  (let [fraud (db/get-fraud id)
        fraud-name (:name fraud)
        meta {:description (str "fraude reportada " fraud-name)
              :title (str "risco de fraude " fraud-name)}]
             (assoc fraud :meta meta)))

(defn dashboard []
  {:last (db/get-frauds {:fraud_limit 5})
   :top (db/get-frauds-asc {:fraud_limit 5})})

(defn all []
  (db/get-frauds {:fraud_limit 200}))

(defn by-person [id]
  (db/frauds-by-person {:fk_person id}))