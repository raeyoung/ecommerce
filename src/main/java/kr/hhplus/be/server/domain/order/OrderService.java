package kr.hhplus.be.server.domain.order;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Optional<Order> getOrder(long orderId, OrderStatus orderStatus) {
        return orderRepository.findByOrderIdAndStatusWithLock(orderId, orderStatus)
                .map(order -> {
                    orderItemRepository.findByOrderId(orderId).forEach(order::addOrderItem);
                    return order;
                });
    }

    /**
     * 상품 주문
     * @param userId
     * @param orderItems
     * @return
     */
    @Transactional
    public Order createOrder(long userId, List<OrderItem> orderItems) {
        Order order = orderRepository.save(Order.create(userId));

        orderItems.stream()
                .map(orderItem -> {
                    orderItem.setOrderId(order.getOrderId());
                    return orderItemRepository.save(orderItem);
                })
                .forEach(order::addOrderItem);

        return order;
    }

    /**
     * 상위 상품 조회
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    @Cacheable(cacheNames = "products", key = "'popularProductsIds'")
    public List<Long> getPopularProducts(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return orderItemRepository.findPopularProducts(startDateTime, endDateTime);
    }

    /**
     * 주문 상태 업데이트
     * @param orderStatus
     * @param order
     */
    public void updateOrderStatus(OrderStatus orderStatus, Order order) {
        order.setStatus(orderStatus);

        orderRepository.save(order);
    }
}
