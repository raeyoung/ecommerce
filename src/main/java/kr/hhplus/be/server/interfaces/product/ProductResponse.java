package kr.hhplus.be.server.interfaces.product;

import lombok.Builder;

@Builder
public record ProductResponse(
        Long id,
        String productName,
        int productPrice,
        int stock
){
    public static ProductResponse of(long id, String productName, int price, int stock) {
        return ProductResponse.builder()
                .id(id)
                .productName(productName)
                .productPrice(price)
                .stock(stock)
                .build();
    }
}
