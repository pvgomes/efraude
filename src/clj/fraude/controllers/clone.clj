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

(defn get-clone [id]
  (let [clone (db/get-clone id)
        clone-phone (:prone clone)
        meta {:description (str "o número " clone-phone " teve o WhatsApp clonado, não transfira valores nem dê informações pessoais")
              :title (str "Whatsapp clonado " clone-phone)
              :url (str "https://efraude.app" (:url (l-clone/url-maker (assoc clone :prefix "/whatsapp-clonado/"))))}]
    (assoc clone :meta meta)))

(defn dashboard []
  {:clones (l-clone/complete-url (db/get-clones {:clone_limit 5}))})

(defn by-person [id]
  (->> (db/clones-by-person {:fk_person id})
       (l-clone/complete-url)))
