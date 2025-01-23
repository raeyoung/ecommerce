package kr.hhplus.be.server.infra.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Optional<Coupon> findAvailableCouponForUpdate(Long couponId) {
        return couponJpaRepository.findAvailableCouponForUpdate(couponId);
    }

    @Override
    public Coupon findByIdWithLock(long couponId) {
        return couponJpaRepository.findByIdWithLock(couponId);
    }

    @Override
    public Coupon findById(long couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }
}
