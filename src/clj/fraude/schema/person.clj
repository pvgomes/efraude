(ns fraude.schema.person
  (:require [schema.core :as s]))

(s/defschema person
  "person structure"
  {:id s/Int
   :name s/Str
   :email s/Str
   :refresh_token s/Str
   :token s/Str
   :password s/Str
   :created_at s/Str
   :updated_at s/Str})
