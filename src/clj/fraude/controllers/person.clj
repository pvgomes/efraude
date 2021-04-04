(ns fraude.controllers.person
  (:require
   [buddy.hashers :as hashers]
   [clojure.java.jdbc :as jdbc]
   [fraude.db.core :as db]))

(defn person-by-email [email]
  (db/person-by-email {:email email}))

(defn create! [name email password]
  (let [password (hashers/derive password)
        refresh_token (hashers/derive email)
        token (hashers/derive refresh_token)]
    (jdbc/with-db-transaction [t-conn db/*db*]
      (if-not (empty? (person-by-email email))
        (throw (ex-info "User already exists!"
                        {:fraude/error-id :duplicate-person
                         :error "User already exists!"})))
      (db/create-person!* t-conn
                          {:name name
                           :email email
                           :refresh_token refresh_token
                           :token token
                           :password password}))))

(defn create-google-social! [id email name]
  (let [refresh_token (hashers/derive email)
        token (hashers/derive refresh_token)
        person  {:name name
                 :email email
                 :google_token id
                 :refresh_token refresh_token
                 :token token}]
    (jdbc/with-db-transaction [t-conn db/*db*]
      (if-not (empty? (person-by-email email))
        (db/update-person-google!* t-conn person)
        (db/create-person-google!* t-conn person)))))

(defn save! [{:keys [name email password]}]
  (create! name email password))

(defn authenticate-person [email password]
  (let [{hashed :password :as person} (db/get-person-for-auth* {:email email})]
    (when (hashers/check password hashed)
      (dissoc person :password))))

(defn get-by-google-id [id]
  (db/person-by-google {:google id}))

(defn handle-google! [{:keys [email id name]}]
  (if-let [person (get-by-google-id id)]
    person
    (if (create-google-social! id email name)
      (get-by-google-id id))))