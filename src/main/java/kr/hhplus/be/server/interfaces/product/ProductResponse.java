package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.domain.product.Product;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse{

    Long id;

    String name;

    Long price;

    Long stock;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
