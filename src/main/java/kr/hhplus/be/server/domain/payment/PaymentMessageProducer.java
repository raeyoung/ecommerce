package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.facade.payment.application.PaymentSuccessEvent;

public interface PaymentMessageProducer {
    void send(String topic, PaymentSuccessEvent Message);
}
