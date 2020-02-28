ALTER TABLE person
ADD COLUMN `image` varchar(2048),
ADD COLUMN `token` varchar(2048),
ADD COLUMN `refresh_token` varchar(2048),
ADD COLUMN `facebook_token` varchar(2048),
ADD COLUMN `google_token` varchar(2048),
ADD COLUMN `additional` text;


ALTER TABLE person MODIFY `password` varchar(100) DEFAULT NULL;


