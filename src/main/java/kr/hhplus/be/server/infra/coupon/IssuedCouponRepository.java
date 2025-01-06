package kr.hhplus.be.server.infra.coupon;

import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {

    Optional<IssuedCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    Page<IssuedCoupon> findByUserId(long userId, Pageable pageable);

    @Query("SELECT new kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse(ic.id, ic.couponId, ic.status, c.issuedAt, c.expirationAt) " +
            "FROM IssuedCoupon ic " +
            "JOIN Coupon c ON ic.couponId = c.id " +
            "WHERE ic.userId = :userId " +
            "ORDER BY c.expirationAt ASC")
    Page<IssuedCouponResponse> findCouponsByUserIdOrderedByExpiration(@Param("userId") long userId, Pageable pageable);
}