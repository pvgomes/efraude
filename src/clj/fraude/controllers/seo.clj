(ns fraude.controllers.seo
  (:require
    [fraude.logic.seo :as l-seo]))

(defn terms []
  (l-seo/terms))

(defn donate []
  (l-seo/donate))

(defn clones []
  (l-seo/clones))

(defn frauds []
  (l-seo/frauds))

(defn report-fraud []
  (l-seo/report-fraud))

(defn report-clone []
  (l-seo/report-clone))

