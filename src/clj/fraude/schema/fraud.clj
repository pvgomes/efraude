(ns fraude.schema.fraud
  (:require [schema.core :as s]))

(s/defschema fraud
  "Fraud structure"
  {:id s/Int
   :name s/Str
   :message s/Str
   :caution s/Str
   :person s/Str ;@todo FIXME should be person schema
   :url s/Str
   :created_at s/Str
   :updated_at s/Str})
