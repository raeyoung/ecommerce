/* user */
-- INSERT INTO `hhplus_test`.`user` (`name`)
-- VALUES
--     ('하헌우'),
--     ('김래영'),
--     ('김영래');
-- INSERT INTO `hhplus_test`.`point` (user_id, current_amount)
-- VALUES (1, 5000);

/* product */
-- insert into `hhplus_test`.`product` (name, price, stock, created_at, updated_at)
-- values
--     ('후드티', 20000, 100, now(), now()),
--     ('구스다운 패딩', 500000, 150, now(), now());

/* Coupon */
insert into `hhplus_test`.`coupon` (name, discount_amount, stock, issued_at, expiration_at)
values
   ('새해맞이 10,000원 할인', 10000, 10, now(), NOW() + INTERVAL 5 DAY),
   ('설날맞이 50,000원 할인', 50000, 10, now(), NOW() + INTERVAL 25 DAY),
   ('휴먼해제 30,000원 할인', 30000, 10, now(), NOW() + INTERVAL 3 DAY);

/* Issued_coupon*/
insert into `hhplus_test`.`issued_coupon` (user_id, coupon_id, status, created_at, updated_at)
values
    (999, 1, 'AVAILABLE', now(), now()),
    (999, 2, 'AVAILABLE', now(), now());