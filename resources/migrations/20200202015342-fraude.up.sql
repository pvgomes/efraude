CREATE TABLE `person`
(`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(100) DEFAULT NULL,
`email` varchar(500) NOT NULL,
`password` varchar(100) NOT NULL,
`created_at` datetime DEFAULT NULL,
`updated_at` datetime DEFAULT NULL,
`image` varchar(2048),
`token` varchar(2048),
`refresh_token` varchar(2048),
`facebook_token` varchar(2048),
`google_token` varchar(2048),
`additional` text,
PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--;;

CREATE TABLE `fraud`
(`id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(200) NOT NULL,
`message` text NOT NULL,
`caution` text DEFAULT NULL,
`url` varchar(2048),
`fk_person` int(11) NOT NULL,
`created_at` datetime DEFAULT NULL,
`updated_at` datetime DEFAULT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `fraud_FK_1` FOREIGN KEY (fk_person) REFERENCES person(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--;;

CREATE TABLE `relevance`
(`id` int(11) NOT NULL AUTO_INCREMENT,
`message` text NOT NULL,
`fk_fraud` int(11) NOT NULL,
`fk_person` int(11) NOT NULL,
`type` ENUM('positive', 'negative'),
`up` int(11) DEFAULT 0,
`down` int(11) DEFAULT 0,
`created_at` datetime DEFAULT NULL,
`updated_at` datetime DEFAULT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `relevance_FK_1` FOREIGN KEY (fk_person) REFERENCES person(id),
CONSTRAINT `relevance_FK_2` FOREIGN KEY (fk_fraud) REFERENCES fraud(id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

