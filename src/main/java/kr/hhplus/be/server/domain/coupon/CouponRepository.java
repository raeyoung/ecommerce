package kr.hhplus.be.server.domain.coupon;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CouponRepository {

    Optional<Coupon> findAvailableCoupon(@Param("couponId") Long couponId);

    Coupon findByIdWithLock(Long couponId);

    Coupon findById(long couponId);

    Coupon save(Coupon coupon);

    Long findStockByCouponId(Long couponId);
}