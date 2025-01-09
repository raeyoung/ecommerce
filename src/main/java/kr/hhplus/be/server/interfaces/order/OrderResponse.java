package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.interfaces.product.ProductResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderResponse (
        Long id,
        int totalPrice,
        String status,
        List<ProductResponse> productList
){
    public static OrderResponse of(long id, int totalPrice, String status, List<ProductResponse> productList) {
        return OrderResponse.builder()
                .id(id)
                .totalPrice(totalPrice)
                .status(status)
                .productList(productList)
                .build();
    }
}
