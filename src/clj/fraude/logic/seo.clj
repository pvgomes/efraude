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

(defn clones []
  {:title "clones de whatsapp"
   :description "telefones que tiveram o whatsapp clonado"})

(defn frauds []
  {:title "fraudes reportadas"
   :description "todas as fraudes reportadas no efraude"})

(defn report-fraud []
  {:title "denuncie um fraude"
   :description "ajude a população a não cair em fraudes, denuncie uma fraude que você foi vítima, ou ficou sabendo da existência"})

(defn report-clone []
  {:title "denuncie um telefone de whatsapp clonado"
   :description "denuncie um whatsapp clonado e ajude mais pessoas a não cairem no golpe"})

(s/defn fraud :- s-fraud/seo
  [fraud :- s-fraud/fraud]
  (let [fraud-name (:name fraud)]
    {:description (str "fraude reportada " fraud-name)
     :title (str fraud-name " - efraude")
     :url (str "https://efraude.app" (:url (l-fraud/url-maker (assoc fraud :prefix "/fraude/"))))}))