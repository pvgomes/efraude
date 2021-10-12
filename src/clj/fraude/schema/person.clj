(ns fraude.schema.person
  (:require [schema.core :as s]))

(def types
  #{"admin"
    "user"})

;will be used soon
(s/defschema Type (apply s/enum types))

(s/defschema person
  "person structure"
  {:id {:schema s/Int
        :eg 32
        :id true
        :doc "person id"}
   :name {:schema s/Str
                 :eg "Paulo"
                 :doc "person's name"}
   :email s/Str
   :refresh_token s/Str
   :token s/Str
   :password s/Str
   :created_at s/Str
   :updated_at s/Str})
