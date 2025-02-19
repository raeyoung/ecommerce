package kr.hhplus.be.server.facade.payment.application;

import lombok.Getter;

@Getter
public class PaymentSuccessEvent {
    private final Long orderId;
    private final Long paymentId;

    public PaymentSuccessEvent(Long orderId, Long paymentId) {
        this.orderId = orderId;
        this.paymentId = paymentId;
    }
}