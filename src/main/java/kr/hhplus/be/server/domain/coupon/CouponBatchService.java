package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.infra.redis.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CouponBatchService {
    private final RedisRepository redisRepository;
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public CouponBatchService(RedisRepository redisRepository, CouponRepository couponRepository, IssuedCouponRepository issuedCouponRepository) {
        this.redisRepository = redisRepository;
        this.couponRepository = couponRepository;
        this.issuedCouponRepository = issuedCouponRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 5000) // 5초마다 실행
    public void processCouponRequests() {
        log.info("============= processCouponRequests Scheduler START =============");

        // Redis에 저장된 모든 쿠폰 ID 가져오기
        Set<String> couponKeys = redisRepository.getKeysByPattern("coupon-*-requests");

        if (couponKeys == null || couponKeys.isEmpty()) {
            log.info("============= processCouponRequests 스케줄러 End =============");
            return;
        }

        Set<Long> couponIds = couponKeys.stream()
                .map(key -> key.replace("coupon-", "").replace("-requests", ""))
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        // 모든 쿠폰 ID에 대해 배치 처리 실행
        for (Long couponId : couponIds) {
            processCoupon(couponId);
        }

        log.info("============= processCouponRequests Scheduler END =============");
    }

    public void processCoupon(Long couponId) {
        Long maxIssuanceCount = couponRepository.findStockByCouponId(couponId);

        // 1. 현재 발급된 쿠폰 수 확인
        Long issuedCount = redisRepository.getSetSize("coupon-"  + couponId + "-issued");
        if (issuedCount != null && issuedCount >= maxIssuanceCount) {
            return;
        }

        // 2. Redis에서 선착순 데이터 가져오기
        List<IssuedCoupon> coupons = issuedCouponRepository.findByCouponId(couponId);
        Set<String> userKeys = redisRepository.getSortedSetRange("coupon-"  + couponId + "-requests", 0, (int) (maxIssuanceCount-1));
        if (userKeys == null || userKeys.isEmpty()) {
            return;
        }

        // 3. DB 저장
        log.info("============= processCoupon DB SAVE START =============");
        Set<IssuedCoupon> issuanceList = userKeys.stream()
                .map(userKey -> {
                    String[] parts = userKey.split(":");
                    return new IssuedCoupon(Long.parseLong(parts[1]), Long.parseLong(parts[0]), CouponStatus.AVAILABLE);
                })
                .collect(Collectors.toSet());

        issuedCouponRepository.saveAll(issuanceList);
        log.info("============= processCoupon DB SAVE END =============");

        // 4. Redis에서 처리된 요청 삭제
        userKeys.forEach(userKey -> redisRepository.removeFromSortedSet("coupon-"  + couponId + "-requests", userKey));
    }
}
