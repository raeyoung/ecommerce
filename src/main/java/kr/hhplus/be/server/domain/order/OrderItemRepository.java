package kr.hhplus.be.server.domain.order;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public interface OrderItemRepository {

    List<OrderItem> findByProductId(Long productId);

    List<Long> findPopularProducts(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<OrderItem> findByOrderId(Long orderId);

    OrderItem save(OrderItem orderItem);
}
