CREATE TABLE `person`
(`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(100) DEFAULT NULL,
`email` varchar(500) NOT NULL,
`password` varchar(100) NOT NULL,
`created_at` datetime DEFAULT CURRENT_TIMESTAMP,
`updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--;;

CREATE TABLE `fraud`
(`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(30) NOT NULL,
`message` text NOT NULL,
`url` varchar(2048),
`fk_person` int(11) NOT NULL,
`created_at` datetime DEFAULT CURRENT_TIMESTAMP,
`updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`id`),
CONSTRAINT `pac_customer_FK_1` FOREIGN KEY (fk_person) REFERENCES person(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--;;

CREATE TABLE `relevance`
(`id` int(11) NOT NULL AUTO_INCREMENT,
`message` text NOT NULL,
`fk_fraud` int(11) NOT NULL,
`fk_person` int(11) NOT NULL,
`up` int(11) DEFAULT 0,
`down` int(11) DEFAULT 0,
`created_at` datetime DEFAULT CURRENT_TIMESTAMP,
`updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`id`),
CONSTRAINT `pac_customer_FK_1` FOREIGN KEY (fk_person) REFERENCES person(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

