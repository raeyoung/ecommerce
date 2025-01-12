package kr.hhplus.be.server.integration.order;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.Point;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.facade.order.OrderFacade;
import kr.hhplus.be.server.global.exception.InvalidException;
import kr.hhplus.be.server.domain.order.OrderItemRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.PointRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.order.OrderProduct;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class OrderIntegrationTest {

    @Autowired
    OrderFacade orderFacade;

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


    List<User> users = new ArrayList<>();

    List<Point> points = new ArrayList<>();

    List<Product> products = new ArrayList<>();

    @BeforeEach
    void setUp() {
        users.add(userRepository.save(User.builder().name("하헌우").build()));
        users.add(userRepository.save(User.builder().name("허재").build()));
        users.add(userRepository.save(User.builder().name("이석범").build()));
        users.add(userRepository.save(User.builder().name("라이언").build()));
        users.add(userRepository.save(User.builder().name("율무").build()));
        users.add(userRepository.save(User.builder().name("김래영").build()));

        points.add(pointRepository.save(Point.builder().userId(1L).currentAmount(0L).build()));
        points.add(pointRepository.save(Point.builder().userId(2L).currentAmount(5000L).build()));
        points.add(pointRepository.save(Point.builder().userId(3L).currentAmount(15000L).build()));
        points.add(pointRepository.save(Point.builder().userId(4L).currentAmount(55000L).build()));
        points.add(pointRepository.save(Point.builder().userId(5L).currentAmount(10000L).build()));
        points.add(pointRepository.save(Point.builder().userId(6L).currentAmount(100000L).build()));

        products.add(productRepository.save(Product.builder().name("롱패딩").price(550000L).stock(20L).build()));
        products.add(productRepository.save(Product.builder().name("맨투맨").price(35000L).stock(15L).build()));
    }

    @Test
    void 동시에_6명이_하나의_물건을_주문하는_동시성_테스트를_진행한다() throws InterruptedException {
        // Given
        ExecutorService executorService = Executors.newFixedThreadPool(users.size());
        CountDownLatch latch = new CountDownLatch(users.size());

        // When
        for(int i = 0; i < users.size(); i++) {
            int index = i;
            executorService.submit(() -> {

                List<OrderProduct> orderProductList = new ArrayList<>();
                orderProductList.add(new OrderProduct(products.get(0).getId(), 2L));

                try {
                    orderFacade.createOrder(users.get(index).getId(), new OrderRequest(orderProductList));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // Then
        List<OrderItem> orderItems = orderItemRepository.findByProductId(products.get(0).getId());

        assertEquals(users.size(), orderItems.size());
        assertEquals(12L, orderItems.stream().mapToLong(OrderItem::getQuantity).sum());
    }

    @Test
    void 상품의_재고부족으로_InvalidException을_반환한다() {
        // Given
        long userId = users.get(0).getId();
        List<OrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(new OrderProduct(products.get(0).getId(), 21L));
        orderProductList.add(new OrderProduct(products.get(1).getId(), 16L));

        // When & Then
        assertThrows(InvalidException.class, () ->  orderFacade.createOrder(userId, new OrderRequest(orderProductList)));
    }
}
