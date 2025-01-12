package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface IssuedCouponRepository {

    Optional<IssuedCoupon> findById(long id);

    Optional<IssuedCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    Page<IssuedCoupon> findByUserId(long userId, Pageable pageable);

    Page<IssuedCouponResponse> findCouponsByUserIdOrderedByExpiration(@Param("userId") long userId, Pageable pageable);

    IssuedCoupon save(IssuedCoupon issuedCoupon);

    void deleteAll();
}