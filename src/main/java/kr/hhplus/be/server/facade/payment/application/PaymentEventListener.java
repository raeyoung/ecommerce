package kr.hhplus.be.server.facade.payment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final DataPlatformService dataPlatformService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentCompleteEventListener(PaymentCompleteEvent event) {
        String message = String.format("사용자(UserId: %d) 결제 성공!", event.getUserId());
        dataPlatformService.sendMessage(message);
    }
}
