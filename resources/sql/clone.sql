-- :name create-clone! :! :n
-- :doc creates a new clone
INSERT INTO clone
(phone, message, fk_person, status, ip, cloned_at, created_at, updated_at)
VALUES (:phone, :message, :fk_person, :status, :ip, :cloned_at, now(), now())

-- :name get-clones :? :*
-- :doc selects all clones
SELECT * from clone
order by updated_at desc
limit :clone_limit

-- :name get-clone :? :1
-- :doc retrieves a clone record given the id
SELECT * FROM clone
WHERE id = :id

-- :name search-clone :? :1
-- :doc retrieves a clones record given the phone
SELECT * FROM clone
WHERE phone LIKE '%:phone%'

-- :name clones-by-person :? :*
-- :doc get clones by person
SELECT * from clone
WHERE fk_person = :fk_person
order by updated_at desc