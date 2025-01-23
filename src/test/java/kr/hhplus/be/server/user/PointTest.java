package kr.hhplus.be.server.user;

import kr.hhplus.be.server.domain.user.Point;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class PointTest {

    @Test
    void 포인트생성_성공() {
        Point point = new Point(1L, 1000L);

        assertThat(point.getUserId()).isEqualTo(1L);
        assertThat(point.getCurrentAmount()).isEqualTo(1000L);
        assertThat(point.getCreatedAt()).isNull();
        assertThat(point.getUpdatedAt()).isNotNull();
    }

    @Test
    void 포인트충전_성공() {
        Point point = Point.builder()
                .userId(1L)
                .currentAmount(1000L)
                .build();

        point.chargePoint(500L);

        assertThat(point.getCurrentAmount()).isEqualTo(1500L);
    }

    @Test
    void 포인트충전_실패_금액이_0이하() {
        Point point = Point.builder()
                .userId(1L)
                .currentAmount(1000L)
                .build();

        assertThatThrownBy(() -> point.chargePoint(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 포인트입니다.");
    }

    @Test
    void 포인트사용_성공() {
        Point point = Point.builder()
                .userId(1L)
                .currentAmount(1000L)
                .build();

        point.usePoint(500L);

        assertThat(point.getCurrentAmount()).isEqualTo(500L);
    }

    @Test
    void 포인트사용_실패_금액이_0이하() {
        Point point = Point.builder()
                .userId(1L)
                .currentAmount(1000L)
                .build();

        assertThatThrownBy(() -> point.usePoint(-100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 포인트입니다.");
    }

    @Test
    void 포인트사용_성공_금액차감() {
        Point point = Point.builder()
                .userId(1L)
                .currentAmount(1000L)
                .build();

        point.usePoint(300L);

        assertThat(point.getCurrentAmount()).isEqualTo(700L);
    }
}

