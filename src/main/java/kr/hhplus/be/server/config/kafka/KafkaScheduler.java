package kr.hhplus.be.server.config.kafka;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.payment.PaymentOutbox;
import kr.hhplus.be.server.domain.payment.PaymentOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaScheduler {

    private final PaymentOutboxService paymentOutboxService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(cron = "0 0/5 * * * *")
    @Transactional
    public void republishMessage() {
        log.info("republishMessage start");

        List<PaymentOutbox> outBoxList = paymentOutboxService.findFailMessages();

        outBoxList.forEach(outbox -> {
            try {
                kafkaTemplate.send(outbox.getEventType() + "-topic", outbox.getPayload()).get(); // 동기적 처리
                outbox.publishedStatus();
                log.info("Message published: {}", outbox.getPayload());
            } catch (Exception e) {
                log.error("Message publish failed: {}", outbox.getPayload(), e);
            }
        });

        log.info("republishMessage end");
    }
}
