package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.domain.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long orderItemId;

    private Long productId;

    private String productName;

    private Long productPrice;

    private Long quantity;

    public static OrderItemResponse from(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .orderItemId(orderItem.getId())
                .productId(orderItem.getProductId())
                .productName(orderItem.getProductName())
                .productPrice(orderItem.getProductPrice())
                .quantity(orderItem.getQuantity())
                .build();
    }
}

