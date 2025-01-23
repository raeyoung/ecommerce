package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long id;

    private Long paymentAmount;

    private PaymentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime paymentAt;

    public static PaymentResponse from(Payment payment) {
        if (payment == null) {
            return null;
        }
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentAmount(payment.getPaymentAmount())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .paymentAt(payment.getPaymentAt())
                .build();
    }
}