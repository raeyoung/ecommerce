package kr.hhplus.be.server.infra.order;

import kr.hhplus.be.server.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByProductId(Long productId);

    @Query("""
        SELECT ol.productId
        FROM OrderItem ol
        WHERE ol.createdAt BETWEEN :startDateTime AND :endDateTime
        GROUP BY ol.productId
        ORDER BY SUM(ol.quantity) DESC
    """)
    List<Long> findPopularProducts(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<OrderItem> findByOrderId(Long orderId);
}
