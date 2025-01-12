package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.global.exception.AlreadyExistsException;
import kr.hhplus.be.server.global.exception.NotFoundException;
import kr.hhplus.be.server.global.exception.OutOfStockException;
import kr.hhplus.be.server.interfaces.coupon.CouponRequest;
import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public CouponService(CouponRepository couponRepository, IssuedCouponRepository issuedCouponRepository) {
        this.couponRepository = couponRepository;
        this.issuedCouponRepository = issuedCouponRepository;
    }

    /**
     * 선착순 쿠폰 발급
     * @param request
     * @return
     */
    @Transactional
    public String issueCoupon(CouponRequest request) {
        // 비관적 락을 사용하여 쿠폰 데이터 조회
        Coupon coupon = couponRepository.findAvailableCouponForUpdate(request.couponId())
                .orElseThrow(() -> new OutOfStockException("쿠폰이 모두 소진되었습니다."));

        // 중복 발급 여부
        if (issuedCouponRepository.findByUserIdAndCouponId(request.userId(), request.couponId()).isPresent()) {
            throw new AlreadyExistsException("이미 해당 쿠폰을 발급받았습니다.");
        }

        // 재고를 감소
        coupon.setStock(coupon.getStock() - 1);

        // 발급된 쿠폰 저장
        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .userId(request.userId())
                .couponId(coupon.getId())
                .status(CouponStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        issuedCouponRepository.save(issuedCoupon);

        return "쿠폰 발급이 완료되었습니다.";
    }

    /**
     * 쿠폰 목록 조회
     * 만료일이 임박한 쿠폰부터 정렬하여 조회한다.
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public Page<IssuedCouponResponse> userCoupons(long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // 사용자가 보유한 쿠폰이 없을 경우 예외 처리
        Page<IssuedCouponResponse> issuedCoupons = issuedCouponRepository.findCouponsByUserIdOrderedByExpiration(userId, pageable);
        if (issuedCoupons.isEmpty()) {
            throw new NotFoundException("사용자가 보유한 쿠폰이 없습니다.");
        }
        return issuedCoupons;
    }

    /**
     * 동시성 고려한 쿠폰 조회
     * @param couponId
     * @return
     */
    public Coupon getCoupon(long couponId) {
        return couponRepository.findByIdWithLock(couponId);
    }

    /**
     * 사용자가 보유한 쿠폰 단건 조회
     * @param couponId
     * @return
     */
    public Optional<IssuedCoupon> userCoupon(long couponId) {
        return issuedCouponRepository.findById(couponId);
    }

    /**
     * 쿠폰 상태 업데이트
     * @param issuedCoupon
     * @return
     */
    public IssuedCoupon updateCouponStatus(IssuedCoupon issuedCoupon) {
        return issuedCouponRepository.save(issuedCoupon);
    }
}
