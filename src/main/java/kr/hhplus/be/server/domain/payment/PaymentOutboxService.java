package kr.hhplus.be.server.domain.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOutboxService {

    private final PaymentOutboxRepository paymentOutboxRepository;

    public void save(PaymentOutbox outbox) {
        paymentOutboxRepository.save(outbox);
    }

    @Transactional
    public void publishedMessage(String data) {
        List<PaymentOutbox> byPayload = paymentOutboxRepository.findByPayload(data);
        byPayload.forEach(PaymentOutbox::publishedStatus);
        log.info("byPayload : {}", byPayload);
    }
}
