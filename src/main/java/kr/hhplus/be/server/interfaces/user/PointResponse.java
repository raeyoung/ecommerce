package kr.hhplus.be.server.interfaces.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.Point;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class PointResponse {

    Long userId;

    Long currentAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;

    public static PointResponse of(Point point) {
        return PointResponse.builder()
                .userId(point.getUserId())
                .currentAmount(point.getCurrentAmount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static PointResponse from(Point point) {
        return PointResponse.builder()
                .userId(point.getUserId())
                .currentAmount(point.getCurrentAmount())
                .createdAt(point.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
