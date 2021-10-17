(ns fraude.controllers.seo
  (:require
    [fraude.logic.seo :as l-seo]))

(defn terms []
  (l-seo/terms))

(defn donate []
  (l-seo/donate))
