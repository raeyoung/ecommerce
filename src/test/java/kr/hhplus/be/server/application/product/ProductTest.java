package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

public class ProductTest {

    @Test
    void 상품_생성_성공() {
        Product product = Product.builder()
                .id(1L)
                .name("맨투맨")
                .price(10000L)
                .stock(50L)
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();

        assertEquals(1L, product.getId());
        assertEquals("맨투맨", product.getName());
        assertEquals(10000L, product.getPrice());
        assertEquals(50L, product.getStock());
    }

    @Test
    void 재고_감소_성공() {
        Product product = Product.builder()
                .stock(50L)
                .build();

        product.reduceStock(10L);

        assertEquals(40L, product.getStock());
    }

    @Test
    void 잘못된_수량으로_재고_감소시_예외() {
        Product product = Product.builder()
                .stock(50L)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> product.reduceStock(0L));
        assertThat("유효하지 않은 수량입니다.").isEqualTo(exception.getMessage());
    }

    @Test
    void 재고_부족으로_감소시_예외() {
        Product product = Product.builder()
                .stock(10L)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> product.reduceStock(20L));
        assertThat("상품 재고가 부족합니다.").isEqualTo(exception.getMessage());
    }

    @Test
    void 재고_부족_상품_생성_테스트() {
        Product product = Product.notAvailable(1L);

        assertEquals(1L, product.getId());
        assertEquals("", product.getName());
        assertEquals(0L, product.getPrice());
        assertEquals(0L, product.getStock());
    }
}

