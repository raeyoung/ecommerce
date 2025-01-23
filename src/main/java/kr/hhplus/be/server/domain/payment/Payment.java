package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long orderId;

    private Long paymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // COMPLETED, FAILED

    private LocalDateTime createdAt;

    private LocalDateTime paymentAt;

    // 결제 성공
    public static Payment ofSuccess(long userId, Order order) {
        return Payment.builder()
                .userId(userId)
                .orderId(order.getOrderId())
                .paymentAmount(order.totalPrice())
                .status(PaymentStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .paymentAt(LocalDateTime.now())
                .build();
    }
}
