package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import kr.hhplus.be.server.global.exception.InvalidException;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_point")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long currentAmount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserPoint(long userId, long currentAmount) {
        this.userId = userId;
        this.currentAmount = currentAmount;
        this.updatedAt = LocalDateTime.now();
    }

    public void chargePoint(long amount) {
        if(amount <= 0) {
            throw new InvalidException("0보다 작은 값을 충전할 수 없습니다.");
        }
        this.currentAmount += amount;
    }

    public void usePoint(long amount) {
        if(amount <= 0) {
            throw new InvalidException("0보다 작은 값을 사용할 수 없습니다.");
        }
        this.currentAmount -= amount;
    }
}
