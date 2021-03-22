ALTER TABLE `clone`
ADD `ip` varchar(100) NOT NULL;

--;;

ALTER TABLE `clone`
MODIFY COLUMN `fk_person` int(11) DEFAULT NULL;