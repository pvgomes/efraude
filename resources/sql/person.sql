-- :name save-person! :! :n
-- :doc creates a new person using the user information
INSERT INTO person
(name, email, password, created_at, updated_at)
VALUES (:name, :email, :password, now(), now())

-- :name create-person!* :! :n
-- :doc creates a new person using the user information
INSERT INTO person
(name, email, refresh_token, token, password, created_at, updated_at)
VALUES (:name, :email, :refresh_token, :token, :password, now(), now())

-- :name create-person-google!* :! :n
-- :doc creates a new person using the user information
INSERT INTO person
(name, email, refresh_token, token, google_token, created_at, updated_at)
VALUES (:name, :email, :refresh_token, :token, :google_token, now(), now())

-- :name update-person-google!* :! :n
-- :doc update person by google informations
UPDATE person
SET google_token = :google_token
WHERE email = :email

-- :name get-persons :? :*
-- :doc selects all persons
SELECT * from person

-- :name get-person :? :1
-- :doc retrieves a person record given the id
SELECT * FROM person
WHERE id = :id

-- :name person-by-email :? :1
-- :doc retrieves a person by e-mail
SELECT * FROM person
where email = :email

-- :name person-by-google :? :1
-- :doc retrieves a person by google token
SELECT * FROM person
where google_token = :google

-- :name person-by-token :? :1
-- :doc retrieves a person by google token
SELECT * FROM person
where token = :token

-- :name update-person! :! :n
-- :doc updates an existing person record
UPDATE person
SET name = :name, email = :email
WHERE id = :id

-- :name get-person-for-auth* :? :1
-- :doc selects a person for authentication
SELECT * FROM person
WHERE email = :email
