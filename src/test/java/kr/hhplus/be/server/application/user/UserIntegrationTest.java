package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.Point;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.PointRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.facade.user.UserFacade;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import kr.hhplus.be.server.interfaces.user.PointResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    UserFacade userFacade;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointRepository pointRepository;

    User user;

    Point point;

    @Test
    void 사용자가_잔액조회에_성공한다() {
        // Given
        user = userRepository.save(User.builder().name("김래영").build());
        point = pointRepository.save(Point.builder().userId(user.getId()).currentAmount(5000L).build());
        Long userId = user.getId();

        // When
        Point point = userService.getPoint(userId);

        // Then
        assertThat(point, is(notNullValue()));
        assertThat(point.getUserId(), is(userId));
        assertThat(point.getCurrentAmount(), is(5000L));
    }

    @Test
    void 사용자가_잔액충전에_성공한다() {
        // Given
        user = userRepository.save(User.builder().name("김래영").build());
        point = pointRepository.save(Point.builder().userId(user.getId()).currentAmount(5000L).build());
        Long userId = user.getId(); // 초기 데이터에 존재하는 사용자 ID
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
        Point userPoint = pointRepository.findByUserId(userId).orElseThrow();
        assertThat(userPoint, is(notNullValue())); // UserPoint가 null이 아닌지 검증
        assertThat(userPoint.getCurrentAmount(), is(expectedAmount)); // DB의 잔액이 예상 값과 같은지 검증
    }

    @Test
    void 동일한_사용자가_동시에_포인트_충전을_5회_요청한_경우_순차적으로_충전하는_동시성_테스트를_진행한다() throws InterruptedException {
        // Given
        user = userRepository.save(User.builder().name("김래영").build());
        point = pointRepository.save(Point.builder().userId(user.getId()).currentAmount(0L).build());

        long userId = user.getId();
        long amount = 1L;
        int tryCount = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(tryCount);
        CountDownLatch latch = new CountDownLatch(tryCount);

        // When
        for (int i = 0; i < tryCount; i++) {
            executorService.submit(() -> {
                try {
                    userFacade.chargePoint(new PointRequest(userId, amount));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        PointResponse response = userFacade.getPoint(userId);

        // Then
        assertEquals(amount * tryCount, response.getCurrentAmount());
    }

}
