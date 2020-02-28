(ns fraude.controllers.fraud
  (:require
    [fraude.db.core :as db]
    [fraude.logic.fraud :as l-fraud]))

(defn save! [{:keys [name message caution fk_person url]}]
  (db/create-fraud! {:name name
                     :message message
                     :caution caution
                     :fk_person fk_person
                     :url url}))

(defn get-fraud [id]
  (let [fraud (db/get-fraud id)
        fraud-name (:name fraud)
        meta {:description (str "fraude reportada " fraud-name)
              :title (str "risco de fraude " fraud-name)
              :url (str "https://efraude.app" (:url (l-fraud/url-maker (assoc fraud :prefix "/fraude/"))))}]
             (assoc fraud :meta meta)))

(defn dashboard []
  {:last (l-fraud/complete-url (db/get-frauds {:fraud_limit 5}))
   :top (l-fraud/complete-url (db/get-top-frauds {:type "positive"
                                                  :limit 5}))})

(defn all []
  (db/get-frauds {:fraud_limit 200}))

(defn by-person [id]
  (db/frauds-by-person {:fk_person id}))


(defn node [fraud]
  (str "<url>
  <loc>https://efraude.app" (:url fraud) "</loc>
  <lastmod>" (.format (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss+00:00") (new java.util.Date)) "</lastmod>
  <priority>1</priority>
  </url>"))

(defn nodes [frauds]
  (->> frauds
       (map node)
       (clojure.string/join "")))

;Multi-Arity
(defn frauds-nodes
  ([] (frauds-nodes (all)))
  ([frauds]
   (->> frauds
         (l-fraud/complete-url)
         (nodes))))

(comment
;make unit test using this data
  (def frauds '({:id 2,
                 :name "Clone do Whatsapp ",
                 :message "<p>Clone do Whatsapp&nbsp;</p>",
                 :caution "<p>Clone do Whatsapp&nbsp;</p>",
                 :url "",
                 :fk_person 1}
                {:id 1,
                 :name "Curso novos ricos",
                 :message "<p>Curso novos ricos</p>",
                 :caution "<p>Curso novos ricos</p>",
                 :url "",
                 :fk_person 1})))