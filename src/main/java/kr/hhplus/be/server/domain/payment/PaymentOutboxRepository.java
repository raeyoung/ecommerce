package kr.hhplus.be.server.domain.payment;

import java.util.List;

public interface PaymentOutboxRepository {

    void save(PaymentOutbox outbox);

    List<PaymentOutbox> findByPayload(String data);
}
