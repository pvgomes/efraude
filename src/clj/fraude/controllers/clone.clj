(ns fraude.controllers.clone
  (:require
    [fraude.db.core :as db]
    [fraude.logic.clone :as l-clone]))

(defn save! [{:keys [phone message cloned_at fk_person]}]
  (db/create-clone! {:phone phone
                     :message message
                     :status "cloned"
                     :cloned_at cloned_at
                     :fk_person fk_person}))