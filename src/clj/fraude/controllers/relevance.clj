(ns fraude.controllers.relevance
  (:require
    [fraude.db.core :as db]))

(defn save! [fk_person fk_fraud type]
  (db/create-relevance! {:message ""
                     :fk_person fk_person
                     :fk_fraud fk_fraud
                     :type type}))
