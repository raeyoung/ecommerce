package kr.hhplus.be.server.facade.payment;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.global.exception.ExceptionMessage;
import kr.hhplus.be.server.interfaces.payment.PaymentRequest;
import kr.hhplus.be.server.interfaces.payment.PaymentResponse;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
    public class PaymentFacade {

        private final OrderService orderService;

        private final PaymentService paymentService;

        private final UserService userService;

        private final CouponService couponService;

        public PaymentFacade(OrderService orderService, PaymentService paymentService, UserService userService, CouponService couponService) {
            this.orderService = orderService;
            this.paymentService = paymentService;
            this.userService = userService;
            this.couponService = couponService;
        }

        @Transactional
        public PaymentResponse payment(long userId, PaymentRequest request) {
            // 주문 검증
            Order order = orderService.getOrder(request.orderId(), OrderStatus.WAITING)
                    .orElseThrow(() -> new IllegalStateException(ExceptionMessage.ORDER_NOT_FOUND.getMessage()));

            if (order.getStatus() == OrderStatus.COMPLETED) {
                throw new IllegalStateException(ExceptionMessage.ORDER_ALREADY_PAYMENT.getMessage());
            }

            // 쿠폰 검증 및 할인 계산
            long discountAmount = 0L; // 초기값
            if (request.couponId() != null) {
                IssuedCoupon issuedCoupon = couponService.userCoupon(request.couponId())
                        .orElseThrow(() -> new IllegalStateException(ExceptionMessage.INVALID_COUPON.getMessage()));

                // 쿠폰을 이미 사용한 경우
                if (issuedCoupon.getStatus().equals(CouponStatus.USED)) {
                    throw new IllegalStateException(ExceptionMessage.COUPON_ALREADY_USED.getMessage());
                }

                Coupon coupon = couponService.getCoupon(issuedCoupon.getCouponId());
                discountAmount = coupon.getDiscountAmount();

                // 쿠폰 상태 업데이트
                couponService.updateCouponStatus(IssuedCoupon.useCoupon(userId, request.couponId()));
            }

            // 포인트 사용
            if (discountAmount > 0) {
                PointRequest pointRequest = new PointRequest(userId, discountAmount);
                userService.usePoint(pointRequest);
            }

            // 주문 상태 업데이트
            orderService.updateOrderStatus(OrderStatus.COMPLETED, order);

            // 결제 처리
            Payment payment = paymentService.processPayment(userId, order);

            sendToPlatform();

            return PaymentResponse.from(payment);
        }

        public void sendToPlatform () {
            System.out.println("결재 정보가 데이터 플랫폼에 전송되었습니다.");
        }
}
