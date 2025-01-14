package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import kr.hhplus.be.server.global.exception.ExceptionMessage;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    private Long stock;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    public static Product notAvailable(long productId) {
        return Product.builder()
                .id(productId)
                .name("")
                .price(0L)
                .stock(0L)
                .build();
    }

    // 상품 수량 감소
    public void reduceStock(long quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_QUANTITY.getMessage());
        }

        if (this.stock < quantity) {
            throw new IllegalArgumentException(ExceptionMessage.INSUFFICIENT_STOCK.getMessage());
        }
        this.stock -= quantity;
    }
}
