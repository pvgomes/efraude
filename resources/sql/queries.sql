-- :name create-fraud! :! :n
-- :doc creates a new fraud using name message and fk_person
INSERT INTO fraud
  (name, message, caution, fk_person, url, created_at, updated_at)
VALUES (:name, :message, :caution, :fk_person, :url, now(), now())

-- :name get-frauds :? :*
-- :doc selects all available messages
select * from fraud
order by updated_at desc
limit :fraud_limit

-- :name get-frauds-asc :? :*
-- :doc selects all available messages
select * from fraud
order by updated_at asc
limit :fraud_limit

-- :name get-fraud :? :1
-- :doc get a fraud by id
SELECT * FROM fraud
WHERE id = :id

-- :name frauds-by-person :? :*
-- :doc get frauds by person
SELECT * from fraud
WHERE fk_person = :fk_person
order by updated_at desc

-- :name save-person! :! :n
-- :doc creates a new person using the user information
INSERT INTO person
  (name, email, password, created_at, updated_at)
VALUES (:name, :email, :password, now(), now())

-- :name create-person!* :! :n
-- :doc creates a new person using the user information
INSERT INTO person
(name, email, password, created_at, updated_at)
VALUES (:name, :email, :password, now(), now())

-- :name get-person-for-auth* :? :1
-- :doc selects a person for authentication
SELECT * FROM person
WHERE email = :email


-- :name get-persons :? :*
-- :doc selects all persons
SELECT * from person

-- :name update-person! :! :n
-- :doc updates an existing person record
UPDATE person
SET name = :name, email = :email
WHERE id = :id

-- :name get-person :? :1
-- :doc retrieves a person record given the id
SELECT * FROM person
WHERE id = :id

-- :name person-by-email :? :1
-- :doc retrieves a person by e-mail
SELECT * FROM person
where email = :email

-- :name create-relevance! :! :n
-- :doc creates as new relevance
INSERT INTO relevance
(message, fk_person, fk_fraud, type, created_at, updated_at)
VALUES (:message, :fk_person, :fk_fraud, :type, now(), now())

-- :name get-relevances :? :*
-- :doc selects all relevances
SELECT * from relevance

-- :name count-relevance :? :1
-- :doc count relevance by type and fraud id
select count(relevance.id)
from relevance
where relevance.fk_fraud = :fraud_id
and relevance.type = :type