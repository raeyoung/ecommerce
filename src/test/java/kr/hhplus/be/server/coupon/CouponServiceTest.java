package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.global.exception.ExceptionMessage;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.IssuedCouponRepository;
import kr.hhplus.be.server.interfaces.coupon.CouponRequest;
import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @InjectMocks
    CouponService couponService;

    @Mock
    CouponRepository couponRepository;

    @Mock
    IssuedCouponRepository issuedCouponRepository;

    @Test
    void 선착순_쿠폰_발급에_성공한다() {
        // Given
        long userId = 1L;
        long couponId = 1L;
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("10% 할인 쿠폰")
                .discountAmount(1000L)
                .stock(10)
                .issuedAt(LocalDateTime.now())
                .expirationAt(LocalDateTime.now().plusDays(7))
                .build();

        when(couponRepository.findAvailableCouponForUpdate(couponId)).thenReturn(Optional.of(coupon));
        when(issuedCouponRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(Optional.empty());

        CouponRequest request = new CouponRequest(userId, couponId);

        // When
        String result = couponService.issueCoupon(request);

        // Then
        assertThat(result).isEqualTo("쿠폰 발급이 완료되었습니다.");
        assertThat(coupon.getStock()).isEqualTo(9); // 재고 감소 확인

        verify(issuedCouponRepository, times(1)).save(any(IssuedCoupon.class));
    }

    @Test
    void 쿠폰이_이미_소진된_경우_OutOfStockException를_반환한다() {
        // Given
        long userId = 1L;
        long couponId = 1L;

        when(couponRepository.findAvailableCouponForUpdate(couponId)).thenReturn(Optional.empty());

        CouponRequest request = new CouponRequest(userId, couponId);

        // When & Then
        assertThatThrownBy(() -> couponService.issueCoupon(request))
                .isInstanceOf(IllegalStateException.class);

        verify(couponRepository, times(1)).findAvailableCouponForUpdate(couponId);
        verify(issuedCouponRepository, never()).save(any());
    }

    @Test
    void 사용자가_이미_쿠폰을_발급받은_경우_COUPON_ALREADY_EXISTED를_반환한다() {
        // Given
        long userId = 1L;
        long couponId = 1L;
        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("10% 할인 쿠폰")
                .discountAmount(1000L)
                .stock(10)
                .issuedAt(LocalDateTime.now())
                .expirationAt(LocalDateTime.now().plusDays(7))
                .build();

        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .status(CouponStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(couponRepository.findAvailableCouponForUpdate(couponId)).thenReturn(Optional.of(coupon));
        when(issuedCouponRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(Optional.of(issuedCoupon));

        CouponRequest request = new CouponRequest(userId, couponId);

        // When & Then
        assertThatThrownBy(() -> couponService.issueCoupon(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ExceptionMessage.COUPON_ALREADY_EXISTED.getMessage());

        verify(issuedCouponRepository, times(1)).findByUserIdAndCouponId(userId, couponId);
        verify(issuedCouponRepository, never()).save(any());
    }

    @Test
    void 사용자가_보유한_쿠폰_목록을_조회한다() {
        // Given
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expirationAt1 = issuedAt.plusDays(5);
        LocalDateTime expirationAt2 = issuedAt.plusDays(10);

        IssuedCoupon issuedCoupon1 = IssuedCoupon.builder()
                .id(1L)
                .userId(1L)
                .couponId(101L)
                .status(CouponStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        IssuedCoupon issuedCoupon2 = IssuedCoupon.builder()
                .id(2L)
                .userId(1L)
                .couponId(102L)
                .status(CouponStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // IssuedCouponResponse 생성
        IssuedCouponResponse issuedCouponResponse1 = IssuedCouponResponse.builder()
                .id(1L)
                .couponId(101L)
                .status(CouponStatus.AVAILABLE)
                .issuedAt(issuedAt)
                .expirationAt(expirationAt1)
                .build();

        IssuedCouponResponse issuedCouponResponse2 = IssuedCouponResponse.builder()
                .id(2L)
                .couponId(102L)
                .status(CouponStatus.AVAILABLE)
                .expirationAt(issuedAt)
                .expirationAt(expirationAt2)
                .build();

        List<IssuedCouponResponse> issuedCoupons = Arrays.asList(issuedCouponResponse1, issuedCouponResponse2);
        Page<IssuedCouponResponse> page = new PageImpl<>(issuedCoupons);

        when(issuedCouponRepository.findCouponsByUserIdOrderedByExpiration(eq(1L), any(Pageable.class))).thenReturn(page);

        // When
        Page<IssuedCouponResponse> result = couponService.getIssuedCoupons(1L, 0, 10);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2); // 총 2개의 쿠폰
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getCouponId()).isEqualTo(101L); // 첫 번째 쿠폰 ID 확인
        assertThat(result.getContent().get(1).getCouponId()).isEqualTo(102L); // 두 번째 쿠폰 ID 확인
        assertThat(result.getContent().get(0).getExpirationAt()).isEqualTo(issuedCouponResponse1.getExpirationAt()); // 첫 번째 쿠폰의 만료일 확인
        assertThat(result.getContent().get(1).getExpirationAt()).isEqualTo(issuedCouponResponse2.getExpirationAt()); // 두 번째 쿠폰의 만료일 확인
    }

    @Test
    void 사용자가_보유한_쿠폰이_없을_경우_COUPON_NOT_FOUND를_반환한다() {
        // Given
        Page<IssuedCouponResponse> page = Page.empty();

        when(issuedCouponRepository.findCouponsByUserIdOrderedByExpiration(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        // When & Then
        assertThatThrownBy(() -> couponService.getIssuedCoupons(1L, 0, 10))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ExceptionMessage.COUPON_NOT_FOUND.getMessage());
    }
}
