(ns fraude.logic.fraud
  (:require [fraude.schema.relevance :as s-relevance]
            [clojure.string :as str]
            [schema.core :as s])
  (:import [java.text Normalizer Normalizer$Form]))

(defn- trim-to [string-to-trim trim-value]
  (apply str (take trim-value string-to-trim)))

(defn- normalize [string-to-normalize]
  (let [normalized (Normalizer/normalize string-to-normalize Normalizer$Form/NFD)
        ascii (str/replace normalized #"[\P{ASCII}]+" "")]
    (str/lower-case ascii)))

(defn slugify
  "Returns a slugified string. Takes two optional parameters:
  delimiter (str): string that interleaves valid words,
  trim-value (int): max url value."
  ([string-to-slugify]           (slugify string-to-slugify "-"))
  ([string-to-slugify delimiter] (slugify string-to-slugify delimiter 250))
  ([string-to-slugify delimiter trim-value]
   (let [normalized (normalize string-to-slugify)
         split-s (str/split (str/triml normalized) #"[\p{Space}\p{P}]+")
         combined (str/join delimiter split-s)]
     (trim-to combined trim-value))))

(defn common-replace [data]
  (let [id (if (string? (:id data))
             (Integer/parseInt (:id data))
             (:id data))]
    (if (> id 50)
      (slugify (:name data))
      (.replaceAll (:name data) "[^A-Za-z0-9]" ""))))

(defn url-maker [entity]
  (assoc entity :url (str
                      (:prefix entity)
                      (:id entity)
                      "/"
                      (common-replace entity))))

(defn common [entities url-prefix]
  (->> (map #(assoc % :prefix url-prefix) entities)
       (map url-maker)
       (vec)))

(defn complete-url [frauds]
  (common frauds "/fraude/"))

(s/defn vote-positive? :- s/Bool
  [relevance :- s-relevance/relevance]
  (if (= "positive" (:type relevance))
    true
    false))

(s/defn vote-negative? :- s/Bool
  [relevance :- s-relevance/relevance]
  (if (= "negative" (:type relevance))
    true
    false))

(def fraud-default-value
  2)

(s/defn fraud-chances
  [relevances :- [s-relevance/relevance]]
  "get fraud relevance
  criteria:
  we use a simple math of sum, each vote has a value of one
  the fraud by itself has a value of two positive votes,
  which means that one fraud with one negative vote has a high chances to be fraud (- 2 1) = 1
  when the votes tie the chances is medium (- 2 2) = 0
  when theres more votes with negative the chances is low (- 2 3) = -1
  "
  (let [chances {:low {:class "is-success" :text "baixa"}
                 :medium {:class "is-warning" :text "media"}
                 :high {:class "is-danger" :text "alta"}}
        quantity-positives (->> relevances
                                (filter vote-positive?)
                                (count)
                                (+ fraud-default-value))
        quantity-negatives (->> relevances
                                (filter vote-negative?)
                                (count))
        balance (- quantity-positives quantity-negatives)]
    (if (zero? balance)
      (:medium chances)
      (if (> balance 0)
        (:high chances)
        (:low chances)))))
