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