-- :name create-fraud! :! :n
-- :doc creates a new fraud using name message and fk_person
INSERT INTO fraud
  (name, message, fk_person, url)
VALUES (:name, :message, :fk_person, :url)

-- :name get-frauds :? :*
-- :doc selects all available messages
SELECT * from fraud

-- :name get-fraud :? :1
-- :doc get a fraud by id
SELECT * FROM fraud
WHERE id = :id

-- :name save-person! :! :n
-- :doc creates a new person using the user information
INSERT INTO person
  (name, email, password)
VALUES (:name, :email, :password)

-- :name create-person!* :! :n
-- :doc creates a new person using the user information
INSERT INTO person
(name, email, password)
VALUES (:name, :email, :password)

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
(message, fk_person, fk_fraud, type)
VALUES (:message, :fk_person, :fk_fraud, :type)

-- :name get-relevances :? :*
-- :doc selects all relevances
SELECT * from relevance