(ns fraude.schema.relevance
  (:require [schema.core :as s]))

(def rtypes
  #{"positive"
    "negative"})

;will be used soon
(s/defschema rtype (apply s/enum rtypes))

(s/defschema relevance
  "relevance structure, the votes on fraud"
  {:id {:schema s/Int
               :eg 32
               :id true
               :doc "relevance id"}
   :message {:schema s/Str
                 :eg "this is really fraud because of blah"
                 :doc "any message, not used nowadays"}
   :type rtype
   :created_at s/Str
   :updated_at s/Str})
