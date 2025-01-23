package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.IssuedCouponRepository;
import kr.hhplus.be.server.interfaces.coupon.CouponRequest;
import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("test")
public class CouponIntegrationTest {

    @Autowired
    CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private IssuedCouponRepository issuedCouponRepository;

    private Coupon coupon;

    @Test
    void 선착순_쿠폰_발급에_성공한다() {
        // Given
        long userId = 1L;
        long couponId = 1L;

        coupon = Coupon.builder()
                .id(couponId)
                .name("설날맞이 10,000원 할인쿠폰")
                .discountAmount(1000L)
                .stock(10)
                .issuedAt(LocalDateTime.now())
                .expirationAt(LocalDateTime.now().plusDays(7))
                .build();
        couponRepository.save(coupon);

        issuedCouponRepository.deleteAll();

        CouponRequest request = new CouponRequest(userId, couponId);

        // When
        String result = couponService.issueCoupon(request);

        // Then
        assertThat(result, is("쿠폰 발급이 완료되었습니다."));

        Coupon updatedCoupon = couponRepository.findById(couponId);
        assertThat(updatedCoupon.getStock(), is(9));

        IssuedCoupon issuedCoupon = issuedCouponRepository.findByUserIdAndCouponId(userId, couponId)
                .orElseThrow();
        assertThat(issuedCoupon.getUserId(), is(userId));
        assertThat(issuedCoupon.getCouponId(), is(couponId));
        assertThat(issuedCoupon.getStatus(), is(CouponStatus.AVAILABLE));
    }

    @Test
    @Transactional
    void 동시에_5명이_하나의_쿠폰을_발급하는_동시성_테스트를_진행한다() throws InterruptedException {
        // Given
        int threadCount = 5; // 동시 요청 스레드 수
        long couponId = 1L; // 테스트할 쿠폰 ID
        int initialStock = 1; // 쿠폰 초기 재고 (1개)

        // 쿠폰 초기화
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("설날맞이 10,000원 할인쿠폰")
                .discountAmount(10000L)
                .stock(initialStock)
                .issuedAt(LocalDateTime.now())
                .expirationAt(LocalDateTime.now().plusDays(7))
                .build();
        couponRepository.save(coupon);

        // 스레드 풀과 CountDownLatch 생성
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 5명의 사용자가 쿠폰 발급을 요청하는 부분
        AtomicInteger successCount = new AtomicInteger(0); // 성공한 요청 수

        // When
        for (int i = 0; i < threadCount; i++) {
            long userId = i + 1; // 각 스레드마다 다른 사용자 ID 사용
            executorService.submit(() -> {
                try {
                    CouponRequest request = new CouponRequest(userId, couponId);
                    couponService.issueCoupon(request);
                    successCount.incrementAndGet(); // 쿠폰 발급 성공한 경우
                } catch (Exception e) {
                    // 예외 발생 시 무시 (중복 발급 또는 재고 소진 예외)
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드가 완료될 때까지 대기
        latch.await(60, TimeUnit.SECONDS); // 대기 시간 제한
        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow(); // 강제 종료
        }

        // Then
        assertThat(successCount.get(), is(1));

        List<IssuedCoupon> issuedCoupons = issuedCouponRepository.findByCouponId(couponId);
        assertThat(issuedCoupons.size(), is(1));

        // 발급된 쿠폰이 저장되었는지 확인
        IssuedCoupon savedCoupon = issuedCoupons.get(0);
        assertThat(savedCoupon.getCouponId(), is(couponId));
        assertThat(savedCoupon.getStatus(), is(CouponStatus.AVAILABLE));

        // 발급된 쿠폰이 1명에게만 발급되었는지 확인
        Set<Long> userIds = issuedCoupons.stream()
                .map(IssuedCoupon::getUserId)
                .collect(Collectors.toSet());
        assertThat(userIds.size(), is(1));
    }

    @Test
    void 사용자가_보유한_쿠폰_목록을_조회한다() {
        // Given
        long userId = 1L;
        int page = 0;
        int size = 10;

        // When
        Page<IssuedCouponResponse> result = couponService.getIssuedCoupons(userId, page, size);

        // Then
        assertThat(result.getContent().size(), is(2)); // 사용자 ID가 1인 사용자는 두 개의 쿠폰을 가지고 있어야 함
        assertThat(result.getContent().get(0).getCouponId(), is(1L)); // 첫 번째 쿠폰 ID가 1이어야 함
        assertThat(result.getContent().get(1).getCouponId(),is(2L)); // 두 번째 쿠폰 ID가 2이어야 함
    }
}
