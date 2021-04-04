(ns fraude.controllers.relevance
  (:require
   [fraude.db.core :as db]))

(defn save! [fk_person fk_fraud type]
  (if-not (empty? (db/get-relevance-by-vote {:fk_person fk_person
                                             :fk_fraud fk_fraud
                                             :type type}))
    (throw (ex-info "You voted already"
                    {:fraude/error-id :duplicate-vote
                     :error "Você já votou"})))
  (db/create-relevance! {:message ""
                         :fk_person fk_person
                         :fk_fraud fk_fraud
                         :type type}))
