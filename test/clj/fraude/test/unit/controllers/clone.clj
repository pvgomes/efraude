(ns fraude.test.unit.controllers.clone
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [fraude.controllers.clone :as c-clone]))

(deftest test-url
  (testing "clone node xml"
    (let [clone {:id        2,
                    :name      "Clone do Whatsapp ",
                    :message   "<p>Clone do Whatsapp&nbsp;</p>",
                    :caution   "<p>Clone do Whatsapp&nbsp;</p>",
                    :url       "/clone-amigo",
                    :fk_person 1}
          expected   (str "<url>
  <loc>https://efraude.app/clone-amigo</loc>
  <lastmod>" (.format (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss+00:00") (new java.util.Date)) "</lastmod>
  <priority>1</priority>
  </url>")]
      (is (= expected (c-clone/node clone))))))
