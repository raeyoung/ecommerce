package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import kr.hhplus.be.server.global.exception.InvalidException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter
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
            throw new InvalidException("유효하지 않는 수량입니다.");
        }

        if (this.stock < quantity) {
            throw new InvalidException("상품 재고가 부족합니다.");
        }
        this.stock -= quantity;
    }
}
