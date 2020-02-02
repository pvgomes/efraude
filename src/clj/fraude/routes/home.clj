(ns fraude.routes.home
  (:require
    [fraude.layout :as layout]
    [fraude.db.core :as db]
    [fraude.controllers.person :as c-person]
    [fraude.controllers.fraud :as c-fraud]
    [fraude.middleware :as middleware]
    [ring.util.response :as res]
    [ring.util.http-response :as response]
    [struct.core :as st]))

(defn logout [req]
  (-> (res/redirect "/")
      (assoc :session {})))

(defn req-user [req]
  "Returns the username of the current session"
  (if (get-in req [:session :identity])
    (get-in req [:session :identity])))

(defn render [{:keys [flash] :as request}]
  (if (req-user request)
    (merge {:messages (db/get-frauds)
            :user     (req-user request)}
           (select-keys flash [:name :message :errors]))
    (merge {:messages (db/get-frauds)}
           (select-keys flash [:name :message :errors]))))

(defn login [{:keys [params session] :as request}]
  (let [email (get-in params [:email])
        password (get-in params [:password])]
    (if-let [person (c-person/authenticate-person email password)]
      (do (let [updated-session (assoc session :identity person)]
            (-> (response/found "/")
                (assoc :session updated-session))))
      (layout/error-page {:status  200
                          :title   "User doesn't exist"
                          :message "This user doesn't exist"}))))

(defn save-person! [{:keys [params]}]
  (c-person/save! params)
  (response/found "/entrar"))

(defn save-fraud! [{:keys [params]}]
  (c-fraud/save! params)
  (response/found "/"))

(defn entrar-page [request]
  (layout/render request "entrar.html" (render request)))

(defn registrar-page [request]
  (layout/render request "registrar.html" (render request)))

(defn fraudes-page [request]
  (layout/render request "fraudes.html" (render request)))

(defn home-page [request]
  (layout/render request "home.html" (render request)))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/entrar" {:get entrar-page}]
   ["/registrar" {:get registrar-page}]
   ["/fraudes" {:get fraudes-page}]
   ["/person/add" {:post save-person!}]
   ["/fraud/add" {:post save-fraud!}]
   ["/login" {:post login}]
   ["/logout" {:get logout}]])
