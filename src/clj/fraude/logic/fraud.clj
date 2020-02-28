(ns fraude.logic.fraud)

(defn common-replace [data]
  (.replaceAll (:name data) "[^A-Za-z0-9]" ""))

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