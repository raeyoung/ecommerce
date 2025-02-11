package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;

public record CouponCacheResponse(
        Long userId,
        Long couponId,
        Boolean isCached
) {
    public static CouponCacheResponse to(Coupon coupon, Long userId, boolean cacheResult) {
        return new CouponCacheResponse(
                userId,
                coupon.getId(),
                cacheResult
        );
    }
}
