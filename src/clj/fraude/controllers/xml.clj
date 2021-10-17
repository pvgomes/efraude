(ns fraude.controllers.xml
  (:require
    [fraude.logic.xml :as l-xml]))


(defn sitemap []
  (l-xml/xml-sitemap))