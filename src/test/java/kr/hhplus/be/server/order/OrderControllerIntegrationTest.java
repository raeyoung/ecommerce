package kr.hhplus.be.server.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.facade.order.OrderFacade;
import kr.hhplus.be.server.interfaces.order.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private OrderFacade orderFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;


    @Test
    @DisplayName("/api/v1/orders 200 OK - 주문 생성")
    void createOrder() throws Exception {
        // Given
        long userId = 1L;

        Product product = productRepository.save(Product.builder().name("롱패딩").price(550000L).stock(20L).build());
        OrderRequest orderRequest = new OrderRequest(List.of(new OrderProduct(1L, 2L)));

        OrderItem orderItem = OrderItem.create(2L, product);
        Order order = Order.create(userId);
        order.addOrderItem(orderItem);
        OrderResponse orderResponse = OrderResponse.to(order);

        // OrderFacade의 createOrder 메서드가 반환할 결과를 Mock 설정
        when(orderFacade.createOrder(eq(userId), any(OrderRequest.class))).thenReturn(orderResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk());
    }
}