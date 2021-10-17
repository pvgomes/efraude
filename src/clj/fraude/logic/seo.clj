(ns fraude.logic.seo
  (:require
    [fraude.logic.fraud :as l-fraud]
    [fraude.schema.fraud :as s-fraud]
    [schema.core :as s]))


(defn terms []
  {:title "termos do uso do efraude"
   :description "o efraude é um site que busca auxiliar a população a se prevenir de fraudes"})

(defn donate []
  {:title "doação"
   :description "ajude o efraude a continuar no ar, doe qualquer quantia"})

(s/defn fraud :- s-fraud/seo
  [fraud :- s-fraud/fraud]
  (let [fraud-name (:name fraud)]
    {:description (str "fraude reportada " fraud-name)
     :title (str fraud-name " - efraude")
     :url (str "https://efraude.app" (:url (l-fraud/url-maker (assoc fraud :prefix "/fraude/"))))}))