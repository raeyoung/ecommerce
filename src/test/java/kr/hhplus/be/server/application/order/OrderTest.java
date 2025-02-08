package kr.hhplus.be.server.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    private Order order;

    @Test
    public void 주문_생성_성공() {
        // given
        Long userId = 1L;

        // when
        Order newOrder = Order.create(userId);

        // then
        assertThat(newOrder.getUserId()).isEqualTo(userId);
        assertThat(newOrder.getStatus()).isEqualTo(OrderStatus.WAITING);
        assertThat(newOrder.getCreatedAt()).isNotNull();
        assertThat(newOrder.getUpdatedAt()).isNotNull();
        assertThat(newOrder.getOrderItems()).isEmpty();
    }

    @Test
    public void 주문_상품_추가_성공() {
        // given
        Product product = Product.builder()
                .id(1L)
                .name("코트")
                .price(500000L)
                .build();
        OrderItem orderItem = OrderItem.create(2L, product);
        order = Order.create(1L);

        // when
        order.addOrderItem(orderItem);

        // then
        assertThat(order.getOrderItems()).hasSize(1);
        assertThat(order.getOrderItems().get(0)).isEqualTo(orderItem);
    }

    @Test
    public void 총_주문금액_계산_성공() {
        // given
        Product product1 = Product.builder()
                .id(1L)
                .name("코트")
                .price(500000L)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("맨투맨")
                .price(30000L)
                .build();

        OrderItem orderItem1 = OrderItem.create(2L, product1);
        OrderItem orderItem2 = OrderItem.create(3L, product2);
        order = Order.create(1L);

        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);

        // when
        Long totalPrice = order.totalPrice();

        // then
        assertThat(totalPrice).isEqualTo(1090000L);
    }
}
