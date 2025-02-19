package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "event_key")
    private String eventKey;

    @Column(nullable = false, name = "event_type")
    private String eventType;

    @Column(nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "outbox_status")
    private PaymentOutboxStatus outboxStatus;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    public void publishedStatus() {
        this.outboxStatus = PaymentOutboxStatus.PUBLISHED;
    }
}
