package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // COMPLETED, WAITING, CANCELLED

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Transient // JPA 매핑에서 제외
    private List<OrderItem> orderItems = new ArrayList<>(); // 메모리에서만 관리

    // 주문 생성
    public static Order create(long userId) {
        return Order.builder()
                .userId(userId)
                .status(OrderStatus.WAITING)
                .orderItems(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // 주문 상품 추가
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    // 총 주문금액 계산
    public Long totalPrice() {
        return orderItems.stream()
                .mapToLong(OrderItem::totalPrice)
                .sum();
    }
}
