package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import kr.hhplus.be.server.global.exception.ExceptionMessage;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "point")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long currentAmount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public Point(long userId, long currentAmount) {
        this.userId = userId;
        this.currentAmount = currentAmount;
        this.updatedAt = LocalDateTime.now();
    }

    public void chargePoint(long amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_AMOUNT.getMessage());
        }
        this.currentAmount += amount;
    }

    public void usePoint(long amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_AMOUNT.getMessage());
        }
        this.currentAmount -= amount;
    }
}
