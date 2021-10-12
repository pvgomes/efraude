(ns fraude.test.unit.logic.fraud
  (:require
   [clojure.test :refer :all]
   [fraude.logic.fraud :as l-fraud]
   [ring.mock.request :refer :all]))

(deftest test-url
  (testing "url slugify"
    (is (= (l-fraud/slugify "crime do zap é um absurdo") "crime-do-zap-e-um-absurdo"))
    (is (= (l-fraud/slugify "quero caféééé" "_") "quero_cafeeee"))))

(deftest test-xml
  (testing "xml without slugify"
    (let [frauds '({:id        2,
                    :name      "Clone do Whatsapp ",
                    :message   "<p>Clone do Whatsapp&nbsp;</p>",
                    :caution   "<p>Clone do Whatsapp&nbsp;</p>",
                    :url       "",
                    :fk_person 1})
          expected [{:id 2,
                     :name "Clone do Whatsapp ",
                     :message "<p>Clone do Whatsapp&nbsp;</p>",
                     :caution "<p>Clone do Whatsapp&nbsp;</p>",
                     :url "/fraude/2/ClonedoWhatsapp",
                     :fk_person 1,
                     :prefix "/fraude/"}]]
      (is (= (l-fraud/complete-url frauds) expected))))

  (testing "xml with slugify"
    (let [frauds '({:id        52,
                    :name      "Clone do Whatsapp ",
                    :message   "<p>Clone do Whatsapp&nbsp;</p>",
                    :caution   "<p>Clone do Whatsapp&nbsp;</p>",
                    :url       "",
                    :fk_person 1})
          expected [{:id 52,
                     :name "Clone do Whatsapp ",
                     :message "<p>Clone do Whatsapp&nbsp;</p>",
                     :caution "<p>Clone do Whatsapp&nbsp;</p>",
                     :url "/fraude/52/clone-do-whatsapp",
                     :fk_person 1,
                     :prefix "/fraude/"}]]
      (is (= (l-fraud/complete-url frauds) expected))))

  (testing "xml full"
    (let [frauds '({:id        1,
                    :name      "Curso novos ricos",
                    :message   "<p>Curso novos ricos</p>",
                    :caution   "<p>Curso novos ricos</p>",
                    :url       "",
                    :fk_person 1}
                   {:id        15,
                    :name      "leilao mula",
                    :message   "<p>Curso novos ricos</p>",
                    :caution   "<p>Curso novos ricos</p>",
                    :url       "",
                    :fk_person 1}
                   {:id        52,
                    :name      "crime do zap zaps é uma safadesa",
                    :message   "<p>clones no zap zaps</p>",
                    :caution   "<p>nao seji burro</p>",
                    :url       "",
                    :fk_person 1}
                   {:id        "58",
                    :name      "crime do zap zaps é uma safadesa",
                    :message   "<p>clones no zap zaps</p>",
                    :caution   "<p>nao seji burro</p>",
                    :url       "",
                    :fk_person 1})
          expected [{:id 1,
                     :name "Curso novos ricos",
                     :message "<p>Curso novos ricos</p>",
                     :caution "<p>Curso novos ricos</p>",
                     :url "/fraude/1/Cursonovosricos",
                     :fk_person 1,
                     :prefix "/fraude/"}
                    {:id 15,
                     :name "leilao mula",
                     :message "<p>Curso novos ricos</p>",
                     :caution "<p>Curso novos ricos</p>",
                     :url "/fraude/15/leilaomula",
                     :fk_person 1,
                     :prefix "/fraude/"}
                    {:id 52,
                     :name "crime do zap zaps é uma safadesa",
                     :message "<p>clones no zap zaps</p>",
                     :caution "<p>nao seji burro</p>",
                     :url "/fraude/52/crime-do-zap-zaps-e-uma-safadesa",
                     :fk_person 1,
                     :prefix "/fraude/"}
                    {:id "58",
                     :name "crime do zap zaps é uma safadesa",
                     :message "<p>clones no zap zaps</p>",
                     :caution "<p>nao seji burro</p>",
                     :url "/fraude/58/crime-do-zap-zaps-e-uma-safadesa",
                     :fk_person 1,
                     :prefix "/fraude/"}]]
      (is (= (l-fraud/complete-url frauds) expected))))

  (testing "xml ids check"
    (let [frauds '({:id        1,
                    :name      "Curso novos ricos",
                    :message   "<p>Curso novos ricos</p>",
                    :caution   "<p>Curso novos ricos</p>",
                    :url       "",
                    :fk_person 1}
                   {:id        15,
                    :name      "leilao mula",
                    :message   "<p>Curso novos ricos</p>",
                    :caution   "<p>Curso novos ricos</p>",
                    :url       "",
                    :fk_person 1}
                   {:id        52,
                    :name      "crime do zap zaps é uma safadesa",
                    :message   "<p>clones no zap zaps</p>",
                    :caution   "<p>nao seji burro</p>",
                    :url       "",
                    :fk_person 1}
                   {:id        "58",
                    :name      "crime do zap zaps é uma safadesa",
                    :message   "<p>clones no zap zaps</p>",
                    :caution   "<p>nao seji burro</p>",
                    :url       "",
                    :fk_person 1})
          expected [{:id 1,
                     :name "Curso novos ricos",
                     :message "<p>Curso novos ricos</p>",
                     :caution "<p>Curso novos ricos</p>",
                     :url "/fraude/1/Cursonovosricos",
                     :fk_person 1,
                     :prefix "/fraude/"}
                    {:id 15,
                     :name "leilao mula",
                     :message "<p>Curso novos ricos</p>",
                     :caution "<p>Curso novos ricos</p>",
                     :url "/fraude/15/leilaomula",
                     :fk_person 1,
                     :prefix "/fraude/"}
                    {:id 52,
                     :name "crime do zap zaps é uma safadesa",
                     :message "<p>clones no zap zaps</p>",
                     :caution "<p>nao seji burro</p>",
                     :url "/fraude/52/crime-do-zap-zaps-e-uma-safadesa",
                     :fk_person 1,
                     :prefix "/fraude/"}
                    {:id "58",
                     :name "crime do zap zaps é uma safadesa",
                     :message "<p>clones no zap zaps</p>",
                     :caution "<p>nao seji burro</p>",
                     :url "/fraude/58/crime-do-zap-zaps-e-uma-safadesa",
                     :fk_person 1,
                     :prefix "/fraude/"}]]
      (is (= (l-fraud/complete-url frauds) expected)))))

(deftest test-chances-to-be-fraud
  (testing "logic height"
    (let [relevances '({:fk_fraud 3, :type "positive"}
                       {:fk_fraud 3, :type "positive"}
                       {:fk_fraud 3, :type "positive"})]

      (is (= {:class "is-danger" :text "alta"} (l-fraud/fraud-chances relevances)))))

  (testing "2 negatives should be medium because of tie rule"
    (let [relevances '({:fk_fraud 3, :type "negative"}
                       {:fk_fraud 3, :type "negative"})]

      (is (= {:class "is-warning" :text "media"} (l-fraud/fraud-chances relevances)))))

  (testing "case of more negatives"
    (let [relevances '({:fk_fraud 3, :type "negative"}
                       {:fk_fraud 3, :type "negative"}
                       {:fk_fraud 3, :type "negative"})]

      (is (= {:class "is-success" :text "baixa"} (l-fraud/fraud-chances relevances)))))

  (testing "case without votes"
    (let [relevances '()]

      (is (= {:class "is-danger" :text "alta"} (l-fraud/fraud-chances relevances))))))