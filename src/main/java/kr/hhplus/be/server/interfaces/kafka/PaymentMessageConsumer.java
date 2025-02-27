package kr.hhplus.be.server.interfaces.kafka;

import kr.hhplus.be.server.domain.payment.PaymentOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentMessageConsumer {

    private final PaymentOutboxService paymentOutboxService;

    @KafkaListener(topics = "payment-topic", groupId = "consumerGroupId")
    public void listener(String data) {
        log.info("payment-topic : {}", data);
        paymentOutboxService.publishedMessage(data);
    }
}
