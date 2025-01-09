package kr.hhplus.be.server.integration.user;

import kr.hhplus.be.server.domain.user.Point;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.infra.user.PointRepository;
import kr.hhplus.be.server.infra.user.UserRepository;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import kr.hhplus.be.server.interfaces.user.PointResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointRepository userPointRepository;

    @Autowired
    PointRepository pointRepository;

    User user;

    Point point;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder().name("하헌우").build());
        point = pointRepository.save(Point.builder().userId(user.getId()).currentAmount(5000L).build());
    }

    @Test
    void 사용자가_잔액조회에_성공한다() {
        // Given
        Long userId = user.getId();

        // When
        PointResponse point = userService.point(userId);

        // Then
        assertThat(point, is(notNullValue()));
        assertThat(point.getUserId(), is(userId));
        assertThat(point.getCurrentAmount(), is(5000L));
        assertThat(point.getName(), is("하헌우"));
    }

    @Test
    void 사용자가_잔액충전에_성공한다() {
        // Given
        Long userId = 1L; // 초기 데이터에 존재하는 사용자 ID
        Long initialAmount = 5000L; // 초기 잔액
        Long chargeAmount = 1000L; // 추가 충전 금액
        Long expectedAmount = initialAmount + chargeAmount; // 충전 후 예상 잔액

        PointRequest request = new PointRequest(userId, chargeAmount);

        // When
        PointResponse response = userService.chargePoint(request);

        // Then
        assertThat(response, is(notNullValue())); // 응답이 null이 아닌지 검증
        assertThat(response.getUserId(), is(userId)); // 응답의 사용자 ID가 올바른지 검증
        assertThat(response.getCurrentAmount(), is(expectedAmount)); // 충전 후 잔액이 예상 값과 같은지 검증

        // 데이터베이스에서 실제 잔액 검증
        Point userPoint = userPointRepository.findByUserId(userId);
        assertThat(userPoint, is(notNullValue())); // UserPoint가 null이 아닌지 검증
        assertThat(userPoint.getCurrentAmount(), is(expectedAmount)); // DB의 잔액이 예상 값과 같은지 검증
    }
}
