package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderItemRepository orderItemRepository;

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
                OrderItem.create(2L, productItem1),
                OrderItem.create(1L, productItem2)
        );

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.save(any(OrderItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Order createdOrder = orderService.createOrder(userId, orderItems);

        // Then
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getUserId()).isEqualTo(userId);
        assertThat(createdOrder.getOrderItems()).hasSize(2);
    }
}