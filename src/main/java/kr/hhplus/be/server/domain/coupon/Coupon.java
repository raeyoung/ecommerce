package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long discountAmount;

    private int stock;

    private LocalDateTime issuedAt;

    private LocalDateTime expirationAt;
}