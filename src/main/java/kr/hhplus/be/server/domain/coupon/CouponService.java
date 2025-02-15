package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.global.exception.ExceptionMessage;
import kr.hhplus.be.server.infra.redis.RedisRepository;
import kr.hhplus.be.server.interfaces.coupon.CouponCacheResponse;
import kr.hhplus.be.server.interfaces.coupon.CouponRequest;
import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;

    public CouponService(CouponRepository couponRepository, IssuedCouponRepository issuedCouponRepository, UserRepository userRepository, RedisRepository redisRepository) {
        this.couponRepository = couponRepository;
        this.issuedCouponRepository = issuedCouponRepository;
        this.userRepository = userRepository;
        this.redisRepository = redisRepository;
    }

    /**
     * 선착순 쿠폰 발급
     * @param request
     * @return
     */
    public String issueCoupon(CouponRequest request) {
        // 쿠폰 데이터 조회
        Coupon coupon = couponRepository.findAvailableCoupon(request.couponId())
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.COUPON_SOLD_OUT.getMessage()));

        // 중복 발급 여부 확인
        if (issuedCouponRepository.findByUserIdAndCouponId(request.userId(), request.couponId()).isPresent()) {
            throw new IllegalStateException(ExceptionMessage.COUPON_ALREADY_EXISTED.getMessage());
        }
        // 재고 감소
        coupon.reduceStock(1);
        couponRepository.save(coupon);

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
    public Page<IssuedCouponResponse> getIssuedCoupons(long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // 사용자가 보유한 쿠폰이 없을 경우 예외 처리
        Page<IssuedCouponResponse> issuedCoupons = issuedCouponRepository.findCouponsByUserIdOrderedByExpiration(userId, pageable);
        if (issuedCoupons.isEmpty()) {
            throw new IllegalStateException(ExceptionMessage.COUPON_NOT_FOUND.getMessage());
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

    /**
     * 쿠폰 사용 여부 검증
     * @param userId
     * @param couponId
     * @return
     */
    public long processCoupon(long userId, Long couponId) {
        if (couponId == null) {
            return 0L;
        }

        IssuedCoupon issuedCoupon = userCoupon(couponId)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.INVALID_COUPON.getMessage()));

        if (issuedCoupon.getStatus().equals(CouponStatus.USED)) {
            throw new IllegalStateException(ExceptionMessage.COUPON_ALREADY_USED.getMessage());
        }

        Coupon coupon = getCoupon(issuedCoupon.getCouponId());
        long discountAmount = coupon.getDiscountAmount();

        updateCouponStatus(IssuedCoupon.useCoupon(userId, couponId));
        return discountAmount;
    }

    @Transactional
    public CouponCacheResponse requestCouponCache(Long userId, Long couponId) {
        Coupon coupon = couponRepository.findByIdWithLock(couponId);
        if(coupon == null) {
            throw new IllegalStateException(ExceptionMessage.INVALID_COUPON.getMessage());
        }

        String setKey = "coupon-"  + couponId + "-issued";
        String zsetKey = "coupon-"  + couponId + "-requests";

        String uniqueUserKey = couponId + ":" + userId;

        // 1. 중복 발급 방지 (SET 확인)
        if (redisRepository.isMemberOfSet(setKey, uniqueUserKey)) {
            throw new IllegalStateException(ExceptionMessage.COUPON_ALREADY_EXISTED.getMessage());
        }

        // 2. 현재 발급된 쿠폰 수 확인
        Long issuedCount = redisRepository.getSetSize(setKey);
        coupon.checkIssuedCount(issuedCount);

        // 3. 쿠폰 유효기간 확인
        coupon.checkExpiryDate();

        // 4. Redis에 쿠폰 요청 저장 (ZSET + SET, TTL 10분 적용)
        redisRepository.addToSortedSet(zsetKey, uniqueUserKey, System.currentTimeMillis(), 10, TimeUnit.MINUTES);
        redisRepository.addToSet(setKey, uniqueUserKey);

        return CouponCacheResponse.to(coupon, userId, true);
    }
}
