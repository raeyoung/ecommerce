package kr.hhplus.be.server.facade.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.interfaces.coupon.CouponRequest;
import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponFacade {

    private final CouponService couponService;

    public CouponFacade(CouponService couponService) {
        this.couponService = couponService;
    }

    public String issueCoupon(CouponRequest request) {
        return couponService.issueCoupon(request);
    }

    public Page<IssuedCouponResponse> userCoupons(long userId, int page, int size) {
        return couponService.userCoupons(userId, page, size);
    }
}
