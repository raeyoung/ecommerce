package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.global.exception.NotFoundException;
import kr.hhplus.be.server.infra.order.OrderItemRepository;
import kr.hhplus.be.server.infra.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Test
    void 사용자가_상품주문을_성공한다() {
        // Given
        long userId = 1L;
        Order order = Order.create(userId);

        Product productItem1 = Product.builder()
                .id(1L)
                .name("구스다운 패딩")
                .price(100000L)
                .stock(5L)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        Product productItem2 = Product.builder()
                .id(1L)
                .name("니트")
                .price(50000L)
                .stock(3L)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
        List<OrderItem> orderItems = List.of(
                OrderItem.create(2, productItem1),
                OrderItem.create(1, productItem2)
        );

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        Order createdOrder = orderService.createOrder(userId, orderItems);

        // Then
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getUserId()).isEqualTo(userId);
        assertThat(createdOrder.getOrderItems()).hasSize(2);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void 사용자_주문_목록_없을경우_NotFoundException를_반환한다() {
        // Given
        Long userId = 1L;
        when(orderRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // When & Then
        assertThatThrownBy(() -> orderService.getOrdersByUserId(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("주문 내역이 없습니다.");
    }
}
