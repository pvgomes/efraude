(ns fraude.schema.fraud
  (:require [schema.core :as s]))


;name, message, caution, fk_person, url, created_at, updated_at

(def fraud
  "Fraud structure"
  {:id s/Int
   :name s/Str
   :message s/Str
   :caution s/Str
   :person s/Str ;@todo FIXME should be person schema
   :url s/Str
   :created_at s/Str
   :updated_at s/Str})
