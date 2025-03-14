package kr.hhplus.be.server.infra.payment;

import kr.hhplus.be.server.domain.payment.PaymentOutbox;
import kr.hhplus.be.server.domain.payment.PaymentOutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutbox, Long> {

    List<PaymentOutbox> findByPayload(String data);

    List<PaymentOutbox> findByOutboxStatus(PaymentOutboxStatus paymentOutboxStatus);
}
