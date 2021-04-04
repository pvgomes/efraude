(ns fraude.schema.clone
  (:require [fraude.schema.person :as s-person]
            [schema.core :as s]))

(s/defschema clone
  "clone structure"
  {:id s/Int
   :phone s/Str
   :person s/Str ;@todo FIXME should be person schema
   :status s/Str
   :ip s/Str
   :cloned_at s/Str
   :created_at s/Str
   :updated_at s/Str})
