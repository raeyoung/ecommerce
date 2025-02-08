package kr.hhplus.be.server.facade.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.global.annotation.RedissonLock;
import kr.hhplus.be.server.interfaces.coupon.CouponCacheResponse;
import kr.hhplus.be.server.interfaces.coupon.CouponRequest;
import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponFacade {

    private final CouponService couponService;
    private final UserService userService;

    public CouponFacade(CouponService couponService, UserService userService) {
        this.couponService = couponService;
        this.userService = userService;
    }

    @RedissonLock(key = "'coupon:' + #request.couponId() + ':user:' + #request.userId()")
    public String issueCoupon(CouponRequest request) {
        return couponService.issueCoupon(request);
    }

    public Page<IssuedCouponResponse> getIssuedCoupons(long userId, int page, int size) {
        return couponService.getIssuedCoupons(userId, page, size);
    }

    @Transactional
    public CouponCacheResponse couponRequestCache(Long userId, Long couponId) {
        return couponService.requestCouponCache(userId, couponId);
    }
}
