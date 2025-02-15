package kr.hhplus.be.server.facade.payment.application;

import kr.hhplus.be.server.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentCompleteEvent {
    private Long userId;
    private Order order;

    public static PaymentCompleteEvent of(Long userId, Order order) {
        return PaymentCompleteEvent.builder()
                .userId(userId)
                .order(order)
                .build();
    }
}
