package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.Point;
import kr.hhplus.be.server.domain.user.PointHistory;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.PointRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import kr.hhplus.be.server.interfaces.user.PointResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PointRepository userPointRepository;

    @Mock
    PointHistoryRepository userPointHistoryRepository;


    @Test
    void 사용자가_잔액조회에_성공한다() {
        // given
        Point userPoint = new Point(1L, 5000L);  // 잔액 5000

        when(userPointRepository.findByUserId(1L)).thenReturn(Optional.of(userPoint));

        // when: 서비스 메서드 호출
        Point point = userService.getPoint(1L);

        // then: 응답 값 검증
        assertThat(point).isNotNull();
        assertThat(point.getUserId()).isEqualTo(1L);
        assertThat(point.getCurrentAmount()).isEqualTo(5000L);
    }

    @Test
    void 존재하지_않는_사용자의_경우_IllegalArgumentException_반환한다() {
        try {
            userService.getPoint(1L);
        } catch (IllegalStateException ex) {
            // then
            assertThat(ex.getMessage()).isEqualTo("사용자를 찾을 수 없습니다.");
        }
    }


    @Test
    void 사용자가_잔액을_충전을_성공한다() {
        // given
        User user = User.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Point userPoint = new Point(1L, 5000L);         // 잔액 5000
        PointRequest request = new PointRequest(1L, 1000L);     // 충전액 1000

        when(userPointRepository.findByUserIdWithLock(1L)).thenReturn(Optional.of(userPoint));

        // when
        PointResponse response = userService.chargePoint(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getCurrentAmount()).isEqualTo(6000L);  // 5000 + 1000

        // verify
        verify(userPointRepository, times(1)).save(userPoint);
        verify(userPointHistoryRepository, times(1)).save(any(PointHistory.class));
    }

    @Test
    void 충전금액이_0보다_작을_때_IllegalStateException을_반환한다() {
        // given
        User user = User.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Point userPoint = new Point(1L, 5000L);         // 잔액 5000
        PointRequest request = new PointRequest(1L, -100L);  // 음수 금액

        // when & then: 예외가 발생하는지 확인
        assertThrows(IllegalStateException.class, () -> {
            userService.chargePoint(request);
        });
    }
}
