package kr.hhplus.be.server.domain.order;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface OrderRepository{

    Optional<Order> findByOrderIdAndStatusWithLock(long orderId, OrderStatus orderStatus);

    Order save(Order order);

    List<Order> findByUserId(Long userId);


}
