(ns fraude.routes.home
  (:require
    [fraude.layout :as layout]
    [fraude.db.core :as db]
    [fraude.controllers.person :as c-person]
    [fraude.controllers.fraud :as c-fraud]
    [fraude.controllers.relevance :as c-relevance]
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

(defn render [{:keys [flash content] :as request} & [params]]
  (if (req-user request)
    (merge params
            {:content content
            :user     (req-user request)}
           (select-keys flash [:name :message :errors]))
    (merge params
           {:content content}
           (select-keys flash [:name :message :errors]))))

(defn login [{:keys [params session] :as request}]
  (let [email (get-in params [:email])
        password (get-in params [:password])]
    (if-let [person (c-person/authenticate-person email password)]
      (do (let [updated-session (assoc session :identity person)]
            (-> (response/found "/")
                (assoc :session updated-session))))
      (layout/render request "entrar.html"
                     (render
                       (assoc request :flash
                                      (assoc params :errors {:login "usuário ou senha inválidos"})))))))

(defn save-person! [{:keys [params] :as request}]
  (c-person/save! params)
  (layout/render request "entrar.html"
                 (render
                   (assoc request :flash
                                  (assoc params :errors {:message "usuário cadastrado"})))))

(defn save-fraud! [{:keys [params]}]
  (c-fraud/save! params)
  (response/found "/"))

(defn relevance-up! [{:keys [params] :as request}]
  (let [user (req-user request)]
    (c-relevance/save! (:id user) (:id params) "positive"))
  (layout/response-raw))

(defn relevance-down! [{:keys [params] :as request}]
  (let [user (req-user request)]
    (c-relevance/save! (:id user) (:id params) "negative"))
  (layout/response-raw))

(defn entrar-page [request]
  (layout/render request "entrar.html" (render request)))

(defn registrar-page [request]
  (layout/render request "registrar.html" (render request)))

(defn fraudes-page [request]
  (layout/render request "fraudes.html" (render (assoc request :content (c-fraud/all)))))

(defn denuncie-page [request]
  (layout/render request "denuncie.html" (render request)))

(defn fraude-page [{:keys [path-params] :as request}]
  (layout/render request "fraude.html" (render (assoc request :content (c-fraud/get-fraud path-params)))))

(defn home-page [request]
  (layout/render request "home.html" (render (assoc request :content (c-fraud/dashboard)))))

(defn termos-page [request]
  (layout/render request "termos.html" (render request)))

(defn denuncias-page [request]
  (layout/render request "denuncias.html"
                 (render (assoc request :content
                                        (c-fraud/by-person (:id (req-user request)))))))

(defn perfil-page [request]
  (layout/render request "perfil.html" (render request)))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/entrar" {:get entrar-page}]
   ["/registrar" {:get registrar-page}]
   ["/fraudes" {:get fraudes-page}]
   ["/denuncie" {:get denuncie-page}]
   ["/termos" {:get termos-page}]
   ["/suas-denuncias" {:get denuncias-page}]
   ["/perfil" {:get perfil-page}]
   ["/fraude/:id/:title" {:get fraude-page}]
   ["/person/add" {:post save-person!}]
   ["/fraud/add" {:post save-fraud!}]
   ["/fraud/up" {:post relevance-up!}]
   ["/fraud/down" {:post relevance-down!}]
   ["/login" {:post login}]
   ["/logout" {:get logout}]])
