(ns fraude.schema.fraud)


(def message-schema
  [[:name
    st/required
    st/string]
   [:fk_person
    st/required
    st/string]
   [:message
    st/required
    st/string
    {:message "message must contain at least 10 characters"
     :validate (fn [msg] (>= (count msg) 10))}]])