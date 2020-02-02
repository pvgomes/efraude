(ns fraude.controllers.person
  (:require
    [buddy.hashers :as hashers]
    [clojure.java.jdbc :as jdbc]
    [fraude.db.core :as db]))

(defn create! [name email password]
  (jdbc/with-db-transaction [t-conn db/*db*]
                            (if-not (empty? (db/get-person-for-auth* t-conn {:email email}))
                              (throw (ex-info "User already exists!"
                                              {:fraude/error-id :duplicate-person
                                               :error "User already exists!"})))
                            (db/create-person!* t-conn
                                                {:name name
                                                 :email email
                                                 :password (hashers/derive password)})))
(defn save! [{:keys [name email password]}]
  (create! name email password))

(defn authenticate-person [email password]
  (let [{hashed :password :as person} (db/get-person-for-auth* {:email email})]
    (when (hashers/check password hashed)
      (dissoc person :password))))