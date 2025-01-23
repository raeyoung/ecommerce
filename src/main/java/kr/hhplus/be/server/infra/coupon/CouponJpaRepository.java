package kr.hhplus.be.server.infra.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId AND c.stock > 0")
    Optional<Coupon> findAvailableCoupon(@Param("couponId") Long couponId);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 설정
    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId")
    Coupon findByIdWithLock(@Param("couponId") long couponId);

    Coupon findById(long couponId);
}
