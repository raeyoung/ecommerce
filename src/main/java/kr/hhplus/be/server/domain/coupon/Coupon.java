package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.global.exception.ExceptionMessage;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long discountAmount;

    private int stock;

    private LocalDateTime issuedAt;

    private LocalDateTime expirationAt;

    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_QUANTITY.getMessage());
        }

        if (this.stock < quantity) {
            throw new IllegalStateException(ExceptionMessage.COUPON_SOLD_OUT.getMessage());
        }

        this.stock -= quantity;
    }

    public void checkExpiryDate() {
        if(this.expirationAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(ExceptionMessage.INVALID_COUPON.getMessage());
        }
    }

    public void checkIssuedCount(Long count) {
        if(this.stock <= count) {
            throw new IllegalStateException(ExceptionMessage.COUPON_SOLD_OUT.getMessage());
        }
    }
}