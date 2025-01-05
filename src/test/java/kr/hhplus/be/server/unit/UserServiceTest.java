package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserPoint;
import kr.hhplus.be.server.domain.user.UserPointHistory;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.global.exception.InvalidException;
import kr.hhplus.be.server.global.exception.NotFoundException;
import kr.hhplus.be.server.infra.user.UserPointHistoryRepository;
import kr.hhplus.be.server.infra.user.UserPointRepository;
import kr.hhplus.be.server.infra.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.UserPointRequest;
import kr.hhplus.be.server.interfaces.user.UserPointResponse;
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
    UserPointRepository userPointRepository;

    @Mock
    UserPointHistoryRepository userPointHistoryRepository;


    @Test
    void 사용자가_잔액조회에_성공한다() {
        // given
        User user = User.builder()
                .id(1L)
                .name("하헌우")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserPoint userPoint = new UserPoint(1L, 5000L);  // 잔액 5000

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userPointRepository.findByUserId(1L)).thenReturn(userPoint);

        // when: 서비스 메서드 호출
        UserPointResponse response = userService.point(1L);

        // then: 응답 값 검증
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getCurrentAmount()).isEqualTo(5000L);
        assertThat(response.getName()).isEqualTo("하헌우");
    }

    @Test
    void 존재하지_않는_사용자의_경우_NotFoundException_반환한다() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        try {
            userService.point(1L);
        } catch (NotFoundException ex) {
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

        UserPoint userPoint = new UserPoint(1L, 5000L);         // 잔액 5000
        UserPointRequest request = new UserPointRequest(1L, 1000L);     // 충전액 1000

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userPointRepository.findByUserId(1L)).thenReturn(userPoint);

        // when
        UserPointResponse response = userService.chargePoint(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getCurrentAmount()).isEqualTo(6000L);  // 5000 + 1000

        // verify
        verify(userPointRepository, times(1)).save(userPoint);
        verify(userPointHistoryRepository, times(1)).save(any(UserPointHistory.class));
    }

    @Test
    void 충전금액이_0보다_작을_때_InvalidException을_반환한다() {
        // given
        User user = User.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        UserPoint userPoint = new UserPoint(1L, 5000L);         // 잔액 5000
        UserPointRequest request = new UserPointRequest(1L, -100L);  // 음수 금액

        // userRepository와 userPointRepository의 mock 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userPointRepository.findByUserId(1L)).thenReturn(userPoint);  // 기존 userPoint를 반환

        // when & then: 예외가 발생하는지 확인
        InvalidException exception = assertThrows(InvalidException.class, () -> {
            userService.chargePoint(request);
        });

        // then: 예외 메시지 검증
        assertThat(exception.getMessage()).isEqualTo("0보다 작은 값을 충전할 수 없습니다.");
    }
}
