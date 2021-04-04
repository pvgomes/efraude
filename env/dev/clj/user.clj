(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
   [clojure.pprint]
   [clojure.spec.alpha :as s]
   [conman.core :as conman]
   [expound.alpha :as expound]
   [fraude.config :refer [env]]
   [fraude.core :refer [start-app]]
   [fraude.db.core]
   [luminus-migrations.core :as migrations]
   [mount.core :as mount]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn start
  "Starts application.
  You'll usually want to run this on startup."
  []
  (mount/start-without #'fraude.core/repl-server))

(defn stop
  "Stops application."
  []
  (mount/stop-except #'fraude.core/repl-server))

(defn restart
  "Restarts application."
  []
  (stop)
  (start))

(defn restart-db
  "Restarts database."
  []
  (mount/stop #'fraude.db.core/*db*)
  (mount/start #'fraude.db.core/*db*)
  (binding [*ns* 'fraude.db.core]
    (conman/bind-connection fraude.db.core/*db* "sql/queries.sql")))

(defn reset-db
  "Resets database."
  []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate
  "Migrates database up for all outstanding migrations."
  []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback
  "Rollback latest database migration."
  []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration
  "Create a new up and down migration file with a generated timestamp and `name`."
  [name]
  (migrations/create name (select-keys env [:database-url])))

