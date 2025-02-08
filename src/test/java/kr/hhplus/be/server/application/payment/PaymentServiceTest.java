package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    void 사용자가_결제에_성공한다() {
        Order order = Order.builder()
                .orderId(1L)
                .userId(1L)
                .orderItems(Arrays.asList(
                        OrderItem.builder()
                                .id(1L)
                                .productId(1L)
                                .productName("구스다운 패딩")
                                .productPrice(500000L)
                                .quantity(1L)
                                .build(),
                        OrderItem.builder()
                                .id(2L)
                                .productId(2L)
                                .productName("후드티")
                                .productPrice(20000L)
                                .quantity(2L)
                                .build()
                ))
                .build();

        // Given
        long userId = 1L;
        Payment mockPayment = Payment.builder()
                .userId(userId)
                .orderId(order.getOrderId())
                .paymentAmount(order.totalPrice())
                .status(PaymentStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .paymentAt(LocalDateTime.now())
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        // When
        Payment result = paymentService.processPayment(userId, order);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getOrderId()).isEqualTo(order.getOrderId());
        assertThat(result.getPaymentAmount()).isEqualTo(order.totalPrice());
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.COMPLETED);

    }
}
