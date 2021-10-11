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

-- :name create-relevance! :! :n
-- :doc creates as new relevance
INSERT INTO relevance
(message, fk_person, fk_fraud, type, created_at, updated_at)
VALUES (:message, :fk_person, :fk_fraud, :type, now(), now())

-- :name get-relevances :? :*
-- :doc selects all relevances
SELECT * from relevance

-- :name get-relevance-by-vote :? :*
-- :doc get relevance by voter and type
SELECT * from relevance
WHERE fk_person = :fk_person
AND type = :type
AND fk_fraud = :fk_fraud

-- :name get-top-frauds :? :*
-- :doc get top frauds by type and limit
select f.id, f.name, count(f.id) from fraud as f
  inner join relevance as r
    on f.id = r.fk_fraud
where r.type = :type
group by f.id, f.name
order by count(f.id) desc
limit :limit;

-- :name count-relevance :? :1
-- :doc count relevance by type and fraud id
select count(relevance.id)
from relevance
where relevance.fk_fraud = :fraud_id
and relevance.type = :type

-- :name get-relevances-by-fraud :? :*
-- :doc get relevances by fraud id
SELECT * from relevance
WHERE fk_fraud = :id