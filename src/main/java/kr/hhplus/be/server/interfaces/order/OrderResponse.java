package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;

public record OrderResponse (
    Long id,
    OrderStatus status,
    Integer productCount
){
    public static OrderResponse to(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getStatus(),
                order.getOrderItems().size()
        );
    }
}