(ns fraude.env
  (:require
   [clojure.tools.logging :as log]
   [fraude.dev-middleware :refer [wrap-dev]]
   [selmer.parser :as parser]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[fraude started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[fraude has shut down successfully]=-"))
   :middleware wrap-dev})
