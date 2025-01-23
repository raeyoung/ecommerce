package kr.hhplus.be.server.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentTest {

    @Test
    public void 결제_성공() {
        // given
        Order order = Order.create(1L);
        Product product = Product.builder()
                .id(1L)
                .name("원피스")
                .price(100000L)
                .build();
        OrderItem orderItem = OrderItem.create(2, product);
        order.addOrderItem(orderItem);

        // when
        Payment payment = Payment.ofSuccess(1L, order);

        // then
        assertThat(payment.getUserId()).isEqualTo(1L);
        assertThat(payment.getOrderId()).isEqualTo(order.getOrderId());
        assertThat(payment.getPaymentAmount()).isEqualTo(order.totalPrice());
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(payment.getCreatedAt()).isNotNull();
        assertThat(payment.getPaymentAt()).isNotNull();
    }

    @Test
    public void 결제금액_검증_성공() {
        // given
        Order order = Order.create(2L);
        Product product1 = Product.builder()
                .id(1L)
                .name("민소매 티셔츠")
                .price(5000L)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("블라우스")
                .price(7000L)
                .build();

        OrderItem orderItem1 = OrderItem.create(1, product1);
        OrderItem orderItem2 = OrderItem.create(2, product2);

        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);

        // when
        Payment payment = Payment.ofSuccess(2L, order);

        // then
        assertThat(payment.getPaymentAmount()).isEqualTo(19000L);
    }
}
