(ns fraude.routes
  (:require
    [cheshire.core :as json]
    [fraude.controllers.clone :as c-clone]
    [fraude.controllers.fraud :as c-fraud]
    [fraude.controllers.person :as c-person]
    [fraude.controllers.relevance :as c-relevance]
    [fraude.controllers.cms :as c-cms]
    [fraude.layout :as layout]
    [fraude.logic.xml :as l-xml]
    [fraude.middleware :as middleware]
    [ring.util.http-response :as response]
    [ring.util.response :as res]))

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

(defn save-clone! [{:keys [params] :as request}]
  (c-clone/save! (assoc params :ip (:remote-addr request)))
  (response/found "/"))

(defn relevance-up! [{:keys [params] :as request}]
  (let [user (req-user request)]
    (c-relevance/save! (:id user) (:id params) "positive"))
  (layout/response-raw))

(defn relevance-down! [{:keys [params] :as request}]
  (let [user (req-user request)]
    (c-relevance/save! (:id user) (:id params) "negative"))
  (layout/response-raw))

(defn sitemap [request]
  (layout/response-xml (l-xml/xml-sitemap)))

(defn entrar-page [request]
  (layout/render request "entrar.html" (render request)))

(defn registrar-page [request]
  (layout/render request "registrar.html" (render request)))

(defn registrar-clone-de-whatsapp-page [request]
  (layout/render request "registrar-clone.html" (render request)))

(defn fraudes-page [request]
  (layout/render request "list.html" (render (assoc request :content
                                                               {:items (c-fraud/all)}))))
(defn clones-page [request]
  (layout/render request "list.html"
                 (render (assoc request :content {:items (c-clone/all)}))))
(defn noticias-page [request]
  (layout/render request "noticias.html" (render request)))

(defn denuncie-page [request]
  (layout/render request "denuncie.html" (render request)))

(defn fraude-page [{:keys [path-params] :as request}]
  (->> (c-fraud/get-fraud path-params)
       (assoc request :content)
       (render)
       (layout/render request "fraude.html")))

(defn clone-page [{:keys [path-params] :as request}]
  (->> (c-clone/get-clone path-params)
       (assoc request :content)
       (render)
       (layout/render request "fraude.html")))

(defn search-clone-page [{:keys [path-params] :as request}]
  (->> (c-clone/search-clone path-params)
       (assoc request :content)
       (render)
       (layout/render request "fraude.html")))

(defn home-page [request]
  (layout/render request "home.html"
                 (render
                   (assoc request :content (merge (c-fraud/dashboard) (c-clone/dashboard))))))

(defn termos-page [request]
  (layout/render request "cms.html"
                 (render (assoc request :content {:cms (c-cms/termos)
                                                  :meta {:title "termos do uso do efraude"}}))))

(defn donate-page [request]
  (layout/render request "cms.html"
                 (render (assoc request :content {:cms (c-cms/donate)
                                                  :meta {:title "ajude o efraude a continuar no ar, doe qualquer quantia"}}))))

(defn denuncias-page [request]
  (layout/render request "denuncias.html"
                 (render (assoc request :content
                                        {:frauds (c-fraud/by-person (:id (req-user request)))
                                         :clones (c-clone/by-person (:id (req-user request)))}))))

(defn perfil-page [request]
  (layout/render request "perfil.html" (render request)))

(defn login-google [{:keys [params session] :as request}]
  (let [person (select-keys (c-person/handle-google! params) [:name :email :token :type :id])
        updated-session (assoc session :identity person)]
    (-> (response/accepted (json/generate-string person))
        (assoc :session updated-session))))

(defn main []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/sitemap" {:get sitemap}]
   ["/entrar" {:get entrar-page}]
   ["/registrar" {:get registrar-page}]
   ["/registrar-clone-de-whatsapp" {:get registrar-clone-de-whatsapp-page}]
   ["/fraudes" {:get fraudes-page}]
   ["/clones-de-whatsapp" {:get clones-page}]
   ["/noticias" {:get noticias-page}]
   ["/denuncie" {:get denuncie-page}]
   ["/termos" {:get termos-page}]
   ["/doe" {:get donate-page}]
   ["/suas-denuncias" {:get denuncias-page}]
   ["/perfil" {:get perfil-page}]
   ["/fraude/:id/:title" {:get fraude-page}]
   ["/whatsapp-clonado/:id/:title" {:get clone-page}]
   ["/busca-whatsapp-clonado/:phone" {:get search-clone-page}]
   ["/person/add" {:post save-person!}]
   ["/fraud/add" {:post save-fraud!}]
   ["/clone/add" {:post save-clone!}]
   ["/fraud/up" {:post relevance-up!}]
   ["/fraud/down" {:post relevance-down!}]
   ["/social/google" {:post login-google}]
   ["/login" {:post login}]
   ["/logout" {:get logout}]])
