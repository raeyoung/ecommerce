package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public interface IssuedCouponRepository {

    Optional<IssuedCoupon> findById(long id);

    List<IssuedCoupon> findByCouponId(long id);

    Optional<IssuedCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    Page<IssuedCoupon> findByUserId(long userId, Pageable pageable);

    Page<IssuedCouponResponse> findCouponsByUserIdOrderedByExpiration(@Param("userId") long userId, Pageable pageable);

    IssuedCoupon save(IssuedCoupon issuedCoupon);

    void deleteAll();

    void saveAll(Set<IssuedCoupon> issuedCouponSet);
}