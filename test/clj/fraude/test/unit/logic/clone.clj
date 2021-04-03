(ns fraude.test.unit.logic.clone
  (:require
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [fraude.logic.clone :as l-clone]))

(deftest test-url
  (testing "url slugify"
    (is (= (l-clone/slugify "celular clonado") "celular-clonado"))
    (is (= (l-clone/slugify "11 981818821 clonado" "_") "11_981818821_clonado"))))
