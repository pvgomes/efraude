(ns fraude.controllers.fraud
  (:require
    [fraude.db.core :as db]))

(defn save! [{:keys [name message fk_person url]}]
  (db/create-fraud! {:name name
                     :message message
                     :fk_person fk_person
                     :url url}))
