(ns fraude.logic.xml
  (:require
   [fraude.controllers.clone :as c-clone]
   [fraude.controllers.fraud :as c-fraud]))

(defn node [item]
  (str "<url>
  <loc>https://efraude.app" (:url item) "</loc>
  <lastmod>" (.format (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss+00:00") (new java.util.Date)) "</lastmod>
  <priority>1</priority>
  </url>"))

(defn nodes [items]
  (->> items
       (map node)
       (clojure.string/join "")))

(defn links-nodes []
  (->> (clojure.set/union (c-fraud/all) (c-clone/all))
       (nodes)))

(defn xml-sitemap []
  (str "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">
  <url>
   <loc>https://efraude.app/</loc>
   <lastmod>2020-02-07T05:24:02+00:00</lastmod>
   <priority>1</priority>
  </url>
  <url>
   <loc>https://efraude.app/termos</loc>
   <lastmod>2020-02-07T05:24:02+00:00</lastmod>
   <priority>1</priority>
  </url>"
       (links-nodes)
       "</urlset>"))