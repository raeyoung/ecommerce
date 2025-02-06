package kr.hhplus.be.server.interfaces.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    private Long orderId;

    private Long productId;

    private String productName;

    private Long productPrice;

    private Long quantity;
}
