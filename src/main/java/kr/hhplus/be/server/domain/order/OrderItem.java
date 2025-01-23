package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orderItem")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long orderId;

    private String productName;

    private Long productPrice;

    private Long quantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 주문 상품 생성
    public static OrderItem create(long quantity, Product product) {
        return OrderItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .quantity(quantity)
                .build();
    }

    public Long totalPrice() {
        return quantity * productPrice;
    }
}
