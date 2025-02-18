package kr.hhplus.be.server.facade.payment.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentCompleteEvent {
    private String topic;
    private Long userId;
    private Long orderId;

    public static PaymentCompleteEvent of(Long userId, Long orderId) {
        return PaymentCompleteEvent.builder()
                .topic("payment-success")
                .userId(userId)
                .orderId(orderId)
                .build();
    }
}
