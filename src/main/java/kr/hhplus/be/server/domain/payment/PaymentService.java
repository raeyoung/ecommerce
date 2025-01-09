package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.infra.payment.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    /**
     * 결제
     * @param userId
     * @param order
     * @return
     */
    @Transactional
    public Payment processPayment(long userId, Order order) {
        return paymentRepository.save(Payment.ofSuccess(userId, order));
    }
}
