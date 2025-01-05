-- CREATE DATABASE `hhplus_test`;
-- CREATE USER ecommerce@'%' IDENTIFIED BY '1q2w3e4r!!';
-- GRANT ALL PRIVILEGES ON ecommerce.* TO ecommerce@'%';
-- FLUSH PRIVILEGES;

DROP TABLE IF EXISTS `hhplus_test`.`user`;
CREATE TABLE `hhplus_test`.`user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(50) DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `hhplus_test`.`user_point`;
CREATE TABLE `hhplus_test`.`user_point` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `current_amount` bigint DEFAULT '0',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `hhplus_test`.`user_point_history`;
CREATE TABLE `hhplus_test`.`user_point_history` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `type` varchar(50) NOT NULL,
    `amount` bigint NOT NULL,
    `current_amount` bigint NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);