package kr.hhplus.be.server.facade.payment.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.payment.PaymentMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentMessageProducer paymentMessageProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void savePaymentOutBox(PaymentSuccessEvent event) {
        // 예약정보 저장
        log.info("savePaymentOutBox : {}", event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePayment(PaymentSuccessEvent event) {
        // 카프카 발행
        log.info("handlePayment : {}", event);
        paymentMessageProducer.send("payment-topic", event);
    }
}
