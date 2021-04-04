(ns fraude.controllers.clone
  (:require
   [fraude.db.core :as db]
   [fraude.logic.clone :as l-clone]))

(defn save! [{:keys [phone message cloned_at fk_person ip]}]
  (let [person_id (if (empty? fk_person)
                    nil
                    fk_person)]
    (db/create-clone! {:phone phone
                       :message message
                       :status "cloned"
                       :cloned_at cloned_at
                       :fk_person person_id
                       :ip ip})))

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

(defn all []
  (->> (db/get-clones {:clone_limit 200})
       (l-clone/complete-url)))

(defn node [clone]
  (str "<url>
  <loc>https://efraude.app" (:url clone) "</loc>
  <lastmod>" (.format (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss+00:00") (new java.util.Date)) "</lastmod>
  <priority>1</priority>
  </url>"))

(defn nodes [clones]
  (->> clones
       (map node)
       (clojure.string/join "")))

;Multi-Arity
(defn clones-nodes
  ([] (clones-nodes (all)))
  ([frauds]
   (->> frauds
        (l-clone/complete-url)
        (nodes))))
