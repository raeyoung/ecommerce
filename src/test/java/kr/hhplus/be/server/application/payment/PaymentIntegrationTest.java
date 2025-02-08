package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderItemRepository;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.Point;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.facade.payment.PaymentFacade;
import kr.hhplus.be.server.facade.user.UserFacade;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.IssuedCouponRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.PointRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.payment.PaymentRequest;
import kr.hhplus.be.server.interfaces.payment.PaymentResponse;
import kr.hhplus.be.server.interfaces.user.PointRequest;
import kr.hhplus.be.server.interfaces.user.PointResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class PaymentIntegrationTest {

    @Autowired
    UserFacade userFacade;

    @Autowired
    PaymentFacade paymentFacade;

    @Autowired
    CouponService couponService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    IssuedCouponRepository issuedCouponRepository;

    @Test
    void 결제에_성공한다() {
        // Given
        User user = userRepository.save(User.builder().name("김래영").build());
        Point point = pointRepository.save(Point.builder().userId(user.getId()).currentAmount(0L).build());
        Product product = productRepository.save(Product.builder().name("후드티").price(50000L).stock(10L).build());
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("설날맞이 10,000원 할인쿠폰")
                .discountAmount(10000L)
                .stock(10)
                .issuedAt(LocalDateTime.now())
                .expirationAt(LocalDateTime.now().plusDays(7))
                .build());
        IssuedCoupon issuedCoupon = issuedCouponRepository.save(IssuedCoupon.builder()
                .userId(user.getId())
                .couponId(coupon.getId())
                .status(CouponStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        Order order = orderRepository.save(Order.create(user.getId()));
        OrderItem orderItem = OrderItem.create(2L, product);
        orderItem.setOrderId(order.getOrderId());
        order.addOrderItem(orderItem);
        orderItemRepository.save(orderItem);

        long userId = user.getId();
        long orderId = order.getOrderId();
        long couponId = issuedCoupon.getId();
        long amount = 200000L;

        userFacade.chargePoint(new PointRequest(userId, amount));

        // When
        PaymentResponse response = paymentFacade.payment(userId, new PaymentRequest(userId, orderId, couponId));

        // Then
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    void 주문이_존재하지_않는_경우_IllegalStateException를_반환한다() {
        // Given
        User user = userRepository.save(User.builder().name("하헌우").build());
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("설날맞이 10,000원 할인쿠폰")
                .discountAmount(10000L)
                .stock(10)
                .issuedAt(LocalDateTime.now())
                .expirationAt(LocalDateTime.now().plusDays(7))
                .build());
        IssuedCoupon issuedCoupon = issuedCouponRepository.save(IssuedCoupon.builder()
                .userId(user.getId())
                .couponId(coupon.getId())
                .status(CouponStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        long userId = user.getId();
        long orderId = 0L;
        long couponId = issuedCoupon.getId();

        // When & Then
        assertThrows(IllegalStateException.class, () -> paymentFacade.payment(userId, new PaymentRequest(userId, orderId, couponId)));
    }

    @Test
    void 동일한_사용자가_동시에_동일한_주문에_대해_5회_결제한_경우_1회만_결제하는_동시성_테스트를_성공한다() throws InterruptedException {
        // Given
        User user = userRepository.save(User.builder().name("하헌우").build());
        Point point = pointRepository.save(Point.builder().userId(user.getId()).currentAmount(0L).build());
        Product product = productRepository.save(Product.builder().name("후드티").price(50000L).stock(10L).build());
        Coupon coupon = couponRepository.save(Coupon.builder()
                .name("설날맞이 10,000원 할인쿠폰")
                .discountAmount(10000L)
                .stock(10)
                .issuedAt(LocalDateTime.now())
                .expirationAt(LocalDateTime.now().plusDays(7))
                .build());
        IssuedCoupon issuedCoupon = issuedCouponRepository.save(IssuedCoupon.builder()
                .userId(user.getId())
                .couponId(coupon.getId())
                .status(CouponStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        Order order = orderRepository.save(Order.create(user.getId()));
        OrderItem orderItem = OrderItem.create(2L, product);
        orderItem.setOrderId(order.getOrderId());
        order.addOrderItem(orderItem);
        orderItemRepository.save(orderItem);

        long userId = user.getId();
        long orderId = order.getOrderId();
        long couponId = issuedCoupon.getId();
        long amount = 200000L;

        userFacade.chargePoint(new PointRequest(userId, amount));

        int tryCount = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(tryCount);
        CountDownLatch latch = new CountDownLatch(tryCount);

        // When
        for (int i = 0; i < tryCount; i++) {
            executorService.submit(() -> {
                try {
                    paymentFacade.payment(userId, new PaymentRequest(userId, orderId, couponId));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        PointResponse pointResponse = userFacade.getPoint(userId);

        // Then
        assertThat(amount - coupon.getDiscountAmount()).isEqualTo(pointResponse.getCurrentAmount());
    }
}