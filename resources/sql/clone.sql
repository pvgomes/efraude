-- :name save-clone! :! :n
-- :doc creates a new clone
INSERT INTO clone
(name, phone, message, fk_person, cloned_at, created_at, updated_at)
VALUES (:name, :phone, :fk_person, :cloned_at, now(), now())

-- :name get-clones :? :*
-- :doc selects all clones
SELECT * from clones

-- :name get-clone :? :1
-- :doc retrieves a clone record given the id
SELECT * FROM clones
WHERE id = :id

-- :name search-clone :? :1
-- :doc retrieves a clones record given the phone
SELECT * FROM clones
WHERE phone LIKE '%:phone%'