package kr.hhplus.be.server.integration.coupon;

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

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("test")
public class CouponServiceIntegrationTest {

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

        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("설날맞이 10,000원 할인쿠폰")
                .discountAmount(1000L)
                .stock(10)
                .issuedAt(LocalDateTime.now())
                .expirationAt(LocalDateTime.now().plusDays(7))
                .build();
        couponRepository.save(coupon);

        issuedCouponRepository.deleteAll();

        assertThat(issuedCouponRepository.findByUserIdAndCouponId(userId, couponId)).isEmpty();

        CouponRequest request = new CouponRequest(userId, couponId);

        // When
        String result = couponService.issueCoupon(request);

        // Then
        assertThat(result).isEqualTo("쿠폰 발급이 완료되었습니다.");

        Coupon updatedCoupon = couponRepository.findById(couponId);
        assertThat(updatedCoupon.getStock()).isEqualTo(9);

        IssuedCoupon issuedCoupon = issuedCouponRepository.findByUserIdAndCouponId(userId, couponId)
                .orElseThrow();
        assertThat(issuedCoupon.getUserId()).isEqualTo(userId);
        assertThat(issuedCoupon.getCouponId()).isEqualTo(couponId);
        assertThat(issuedCoupon.getStatus()).isEqualTo(CouponStatus.AVAILABLE);
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
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(2); // 사용자 ID가 1인 사용자는 두 개의 쿠폰을 가지고 있어야 함
        assertThat(result.getContent().get(0).getCouponId()).isEqualTo(1L); // 첫 번째 쿠폰 ID가 1이어야 함
        assertThat(result.getContent().get(1).getCouponId()).isEqualTo(2L); // 두 번째 쿠폰 ID가 2이어야 함
    }
}
