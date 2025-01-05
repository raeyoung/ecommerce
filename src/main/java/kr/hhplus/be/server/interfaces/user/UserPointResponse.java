package kr.hhplus.be.server.interfaces.user;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserPoint;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserPointResponse {

    Long userId;

    String name;

    Long currentAmount;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    public static UserPointResponse of(UserPoint point, User user) {
        return UserPointResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .currentAmount(point.getCurrentAmount())
                .build();
    }

    public static UserPointResponse from(UserPoint point) {
        return UserPointResponse.builder()
                .userId(point.getUserId())
                .currentAmount(point.getCurrentAmount())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
