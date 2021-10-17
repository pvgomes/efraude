(ns fraude.controllers.fraud
  (:require
   [fraude.db.core :as db]
   [fraude.logic.fraud :as l-fraud]
   [fraude.logic.seo :as l-seo]))

(defn save! [{:keys [name message caution fk_person url]}]
  (db/create-fraud! {:name name
                     :message message
                     :caution caution
                     :fk_person fk_person
                     :url url}))

(defn get-fraud [id]
  (let [fraud (db/get-fraud id)
        meta (l-seo/fraud fraud)
        fraud-chances (l-fraud/fraud-chances (db/get-relevances-by-fraud id))]
    (-> fraud
        (assoc :meta meta)
        (assoc :fraud-chances fraud-chances))))

(defn dashboard []
  {:last (l-fraud/complete-url (db/get-frauds {:fraud_limit 5}))
   :top (l-fraud/complete-url (db/get-top-frauds {:type "positive"
                                                  :limit 5}))})

(defn all []
  (->> (db/get-frauds {:fraud_limit 200})
       (l-fraud/complete-url)))

(defn by-person [id]
  (->> (db/frauds-by-person {:fk_person id})
       (l-fraud/complete-url)))

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
