package kr.hhplus.be.server.interfaces.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.payment.PaymentMessageProducer;
import kr.hhplus.be.server.facade.payment.application.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMessageProducerImpl implements PaymentMessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(String kafkaTopic, PaymentSuccessEvent message) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(kafkaTopic, jsonInString);
    }
}

