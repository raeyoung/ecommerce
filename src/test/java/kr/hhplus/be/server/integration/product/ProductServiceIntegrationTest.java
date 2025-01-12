package kr.hhplus.be.server.integration.product;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderItemRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.Point;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.facade.product.ProductFacade;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.PointRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class ProductServiceIntegrationTest {

    @Autowired
    ProductFacade productFacade;

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

    User user;

    Point point;

    List<Product> products = new ArrayList<>();

    Order order;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder().name("하헌우").build());

        point = pointRepository.save(Point.builder().userId(1L).currentAmount(10000L).build());
        products = productRepository.findAll();

        if(products.isEmpty()) {
            productRepository.save(Product.builder().name("버버리코트").price(1500000L).stock(50L).created_at(LocalDateTime.now()).updated_at(LocalDateTime.now()).build());
            productRepository.save(Product.builder().name("청바지").price(150000L).stock(30L).created_at(LocalDateTime.now()).updated_at(LocalDateTime.now()).build());
            productRepository.save(Product.builder().name("맨투맨").price(50000L).stock(20L).created_at(LocalDateTime.now()).updated_at(LocalDateTime.now()).build());
            products = productRepository.findAll();
        }

        order = orderRepository.save(Order.create(user.getId()));

        order.addOrderItem(OrderItem.create(3L, products.get(0)));
        order.addOrderItem(OrderItem.create(2L, products.get(1)));
        order.addOrderItem(OrderItem.create(1L, products.get(2)));

        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrderId(order.getOrderId());
            orderItemRepository.save(orderItem);
        }
    }

    @Test
    void 상품_목록을_조회에_성공한다() {
        // When
        int page = 0;
        String criteria = "price";
        String sort = "DESC";
        Page<ProductResponse> products = productFacade.products(page, criteria, sort);  // 상품 금액 기준 내림차순 정렬

        // Then
        assertThat(products)
                .hasSize(3)
                .extracting(i -> tuple(i.getId(), i.getName(), i.getStock(), i.getPrice()))
                .containsExactlyElementsOf(
                        this.products.stream()
                                .map(product -> tuple(product.getId(), product.getName(), product.getStock(), product.getPrice()))
                                .toList()
                );
    }

    @Test
    void 상위_상품_목록_조회에_성공한다() {
        System.out.println(order);
        // When
        List<ProductResponse> response = productFacade.popularProducts();

        // Then
        assertThat(response)
                .hasSize(3)
                .extracting(i -> tuple(i.getId()))
                .containsExactlyElementsOf(
                        products.stream()
                                .map(product -> tuple(product.getId()))
                                .toList()
                );
    }
}
