package kr.hhplus.be.server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {
    INVALID_AMOUNT("유효하지 않은 포인트입니다."),
    COUPON_SOLD_OUT("쿠폰이 모두 소진되었습니다."),
    COUPON_ALREADY_EXISTED("이미 해당 쿠폰을 발급받았습니다."),
    COUPON_NOT_FOUND("사용자가 보유한 쿠폰이 없습니다."),
    INVALID_COUPON("유효하지 않은 쿠폰입니다."),
    COUPON_ALREADY_USED("이미 사용된 쿠폰입니다."),
    PRODUCT_NOT_FOUND("상품이 존재하지 않습니다."),
    INVALID_QUANTITY("유효하지 않은 수량입니다."),
    INSUFFICIENT_STOCK("상품 재고가 부족합니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    POINT_NOT_FOUND("해당 사용자의 포인트 정보를 찾을 수 없습니다."),
    ORDER_NOT_FOUND("존재하지 않는 주문입니다."),
    ORDER_ALREADY_PAYMENT("이미 결제된 주문입니다."),
    PRODUCT_REDUCE_FAILED("상품 재고 감소에 실패하였습니다."),
    REDIS_LOCK_ACQUIRE_FAILED("레디스 락 획득에 실패하였습니다.");

    String message;
}
