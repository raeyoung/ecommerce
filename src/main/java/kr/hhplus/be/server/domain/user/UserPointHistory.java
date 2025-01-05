package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import kr.hhplus.be.server.global.exception.InvalidException;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_point_history")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private PointType type;     // CHARE, USE

    private Long amount;

    private Long currentAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public UserPointHistory(long userId, long amount) {
        this.userId = userId;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static UserPointHistory createCharge(long userId, long amount) {
        return UserPointHistory.builder()
                .userId(userId)
                .amount(amount)
                .type(PointType.CHARGE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static UserPointHistory createUse(long userId, long amount) {
        return UserPointHistory.builder()
                .userId(userId)
                .amount(amount)
                .type(PointType.USE)
                .build();
    }
}
