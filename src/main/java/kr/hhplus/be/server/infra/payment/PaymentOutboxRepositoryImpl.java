package kr.hhplus.be.server.infra.payment;

import kr.hhplus.be.server.domain.payment.PaymentOutbox;
import kr.hhplus.be.server.domain.payment.PaymentOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    @Override
    public void save(PaymentOutbox outbox) {
        paymentOutboxJpaRepository.save(outbox);
    }

    @Override
    public List<PaymentOutbox> findByPayload(String data) {
        return paymentOutboxJpaRepository.findByPayload(data);
    }
}
