package kr.hhplus.be.server.facade.payment;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.facade.payment.application.PaymentEventPublisher;
import kr.hhplus.be.server.facade.payment.application.PaymentSuccessEvent;
import kr.hhplus.be.server.interfaces.payment.PaymentRequest;
import kr.hhplus.be.server.interfaces.payment.PaymentResponse;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentFacade {

    private final PaymentEventPublisher paymentEventPublisher;

    private final OrderService orderService;

    private final PaymentService paymentService;

    private final UserService userService;

    private final CouponService couponService;

    public PaymentFacade(PaymentEventPublisher paymentEventPublisher, OrderService orderService, PaymentService paymentService, UserService userService, CouponService couponService) {
        this.paymentEventPublisher = paymentEventPublisher;
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

        paymentEventPublisher.success(new PaymentSuccessEvent(payment.getOrderId(), payment.getId()));

        return PaymentResponse.from(payment);
    }
}
