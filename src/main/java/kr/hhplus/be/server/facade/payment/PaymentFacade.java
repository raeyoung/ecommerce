package kr.hhplus.be.server.facade.payment;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.payment.event.PaymentCompleteEvent;
import kr.hhplus.be.server.domain.payment.event.PaymentEventHandler;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.payment.PaymentRequest;
import kr.hhplus.be.server.interfaces.payment.PaymentResponse;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentFacade {

    private final PaymentEventHandler eventHandler;

    private final OrderService orderService;

    private final PaymentService paymentService;

    private final UserService userService;

    private final CouponService couponService;

    public PaymentFacade(PaymentEventHandler eventHandler, OrderService orderService, PaymentService paymentService, UserService userService, CouponService couponService) {
        this.eventHandler = eventHandler;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.userService = userService;
        this.couponService = couponService;
    }

    @Transactional
    public PaymentResponse payment(long userId, PaymentRequest request) {
        // 주문 검증
        Order order = orderService.getValidatedOrder(request.orderId(), OrderStatus.WAITING);

        // 쿠폰 처리
        long discountAmount = couponService.processCoupon(userId, request.couponId());

        // 포인트 사용
        if (discountAmount > 0) {
            PointRequest pointRequest = new PointRequest(userId, discountAmount);
            userService.usePoint(pointRequest);
        }

        // 주문 상태 업데이트
        orderService.updateOrderStatus(OrderStatus.COMPLETED, order);

        // 결제 처리
        Payment payment = paymentService.processPayment(userId, order);

        eventHandler.paymentCompleteEventHandler(PaymentCompleteEvent.of(userId, order));

        return PaymentResponse.from(payment);
    }
}
