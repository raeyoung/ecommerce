package kr.hhplus.be.server.infra.coupon;

import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.IssuedCouponRepository;
import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {

    private final IssuedCouponJpaRepository issuedCouponJpaRepository;

    @Override
    public Optional<IssuedCoupon> findById(long id) {
        return issuedCouponJpaRepository.findById(id);
    }

    @Override
    public List<IssuedCoupon> findByCouponId(long id) {
        return issuedCouponJpaRepository.findByCouponId(id);
    }

    @Override
    public Optional<IssuedCoupon> findByUserIdAndCouponId(Long userId, Long couponId) {
        return issuedCouponJpaRepository.findByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public Page<IssuedCoupon> findByUserId(long userId, Pageable pageable) {
        return issuedCouponJpaRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<IssuedCouponResponse> findCouponsByUserIdOrderedByExpiration(long userId, Pageable pageable) {
        return issuedCouponJpaRepository.findCouponsByUserIdOrderedByExpiration(userId, pageable);
    }

    @Override
    public IssuedCoupon save(IssuedCoupon issuedCoupon) {
        return issuedCouponJpaRepository.save(issuedCoupon);
    }

    @Override
    public void deleteAll() {
        issuedCouponJpaRepository.deleteAll();
    }
}
