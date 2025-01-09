-- User
DROP TABLE IF EXISTS `hhplus_test`.`user`;
CREATE TABLE `hhplus_test`.`user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(50) DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `version`   BIGINT      NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `hhplus_test`.`point`;
CREATE TABLE `hhplus_test`.`point` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `current_amount` bigint DEFAULT '0',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `hhplus_test`.`point_history`;
CREATE TABLE `hhplus_test`.`point_history` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `type` varchar(50) NOT NULL,
    `amount` bigint NOT NULL,
    `current_amount` bigint NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

-- Product
DROP TABLE IF EXISTS `hhplus_test`.`product`;
CREATE TABLE `hhplus_test`.`product` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(100) DEFAULT NULL,
    `price` bigint DEFAULT NULL,
    `stock` int DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

-- Coupon
DROP TABLE IF EXISTS `hhplus_test`.`coupon`;
CREATE TABLE `hhplus_test`.`coupon` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(100) DEFAULT NULL,
    `discount_amount` bigint DEFAULT NULL,
    `stock` int DEFAULT NULL,
    `issued_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `expiration_at` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
);

-- issued_coupon
DROP TABLE IF EXISTS `hhplus_test`.`issued_coupon`;
CREATE TABLE `hhplus_test`.`issued_coupon` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint DEFAULT NULL,
    `coupon_id` bigint DEFAULT NULL,
    `payment_id` bigint DEFAULT NULL,
    `status` varchar(50) DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);
DROP TABLE IF EXISTS `hhplus_test`.`orders`;
CREATE TABLE `hhplus_test`.`orders` (
   `order_id` bigint NOT NULL AUTO_INCREMENT,
   `user_id` bigint DEFAULT NULL,
   `status` varchar(50) DEFAULT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`order_id`)
);

DROP TABLE IF EXISTS `hhplus_test`.`order_item`;
CREATE TABLE `hhplus_test`.`order_item` (
    `id`    BIGINT  NOT NULL    AUTO_INCREMENT,
    `order_id`   BIGINT  NOT NULL,
    `product_id`   BIGINT  NOT NULL,
    `product_name`   VARCHAR(100)  NOT NULL,
    `product_price`    BIGINT  NOT NULL,
    `quantity`  BIGINT NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `hhplus_test`.`payment`;
CREATE TABLE `hhplus_test`.`payment` (
    `id` BIGINT  NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `order_id` BIGINT NOT NULL,
    `payment_amount` FLOAT NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `payment_at` TIMESTAMP DEFAULT NULL,
    PRIMARY KEY (`id`)
);