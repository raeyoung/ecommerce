package kr.hhplus.be.server.interfaces.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class IssuedCouponResponse{

    private Long id;

    private Long couponId;

    private CouponStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issuedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationAt;

    // 생성자
    public IssuedCouponResponse(Long id, Long couponId, CouponStatus status, LocalDateTime issuedAt, LocalDateTime expirationAt) {
        this.id = id;
        this.couponId = couponId;
        this.status = status;
        this.issuedAt = issuedAt;
        this.expirationAt = expirationAt;
    }

}

