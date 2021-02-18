CREATE TABLE `clone`
(`id` int(11) NOT NULL AUTO_INCREMENT,
`phone` varchar(100) NOT NULL,
`message` text DEFAULT NULL,
`fk_person` int(11) NOT NULL,
`cloned_at` datetime DEFAULT NULL,
`created_at` datetime DEFAULT NULL,
`updated_at` datetime DEFAULT NULL,
PRIMARY KEY (`id`),
INDEX `clone_phone_index` (`phone`),
CONSTRAINT `clone_FK_1` FOREIGN KEY (fk_person) REFERENCES person(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


