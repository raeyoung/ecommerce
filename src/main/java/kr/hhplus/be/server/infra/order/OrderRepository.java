package kr.hhplus.be.server.infra.order;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 설정
    @Query("SELECT o FROM Order o WHERE o.orderId = :orderId AND o.status = :orderStatus")
    Optional<Order> findByOrderIdAndStatusWithLock(long orderId, OrderStatus orderStatus);

    OrderItem save(OrderItem orderItem);

    List<Order> findByUserId(Long userId);

}
