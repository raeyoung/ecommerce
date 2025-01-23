package kr.hhplus.be.server.infra.order;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public List<OrderItem> findByProductId(Long productId) {
        return orderItemJpaRepository.findByProductId(productId);
    }

    @Override
    public List<Long> findPopularProducts(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return orderItemJpaRepository.findPopularProducts(startDateTime, endDateTime);
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemJpaRepository.findByOrderId(orderId);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemJpaRepository.save(orderItem);
    }
}
