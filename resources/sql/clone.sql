-- :name create-clone! :! :n
-- :doc creates a new clone
INSERT INTO clone
(phone, message, status, fk_person, cloned_at, created_at, updated_at)
VALUES (:phone, :message, :status, :fk_person, :cloned_at, now(), now())

-- :name get-clones :? :*
-- :doc selects all clones
SELECT * from clone

-- :name get-clone :? :1
-- :doc retrieves a clone record given the id
SELECT * FROM clone
WHERE id = :id

-- :name search-clone :? :1
-- :doc retrieves a clones record given the phone
SELECT * FROM clone
WHERE phone LIKE '%:phone%'