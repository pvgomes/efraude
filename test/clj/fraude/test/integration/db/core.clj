(ns fraude.test.integration.db.core
  (:require
   [fraude.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer :all]
   [clojure.java.jdbc :as jdbc]
   [fraude.config :refer [env]]
   [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'fraude.config/env
     #'fraude.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-messages
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1 (db/save-person!
              t-conn
              {:name "Sam"
               :email  "sam@gmail.com"
               :password "123"})))
    (is (= {:name "Sam"
            :email  "sam@gmail.com"}
           (-> (db/get-persons t-conn {})
               (first)
               (select-keys [:name :email]))))))


