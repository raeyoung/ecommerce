package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "issuedCoupon")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long couponId;

    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;  // AVAILABLE, USED, EXPIRED

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
