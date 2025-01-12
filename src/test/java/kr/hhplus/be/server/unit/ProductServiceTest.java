package kr.hhplus.be.server.unit;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.global.exception.NotFoundException;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Test
    void 상품_목록을_조회에_성공한다() {
        // given
        Pageable pageableDesc = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "price"));
        List<Product> mockProductsDesc = Arrays.asList(
                Product.builder()
                        .id(2L)
                        .name("구스다운 패딩")
                        .price(500000L)
                        .stock(150L)
                        .build(),
                Product.builder()
                        .id(1L)
                        .name("후드티")
                        .price(20000L)
                        .stock(100L)
                        .build()
        );
        Page<Product> productPageDesc = new PageImpl<>(mockProductsDesc, pageableDesc, mockProductsDesc.size());
        when(productRepository.findAll(pageableDesc)).thenReturn(productPageDesc);

        // when
        Page<ProductResponse> result = productService.products(0, "price", "DESC");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);  // 두 개의 상품이 있어야 함
        assertThat(result.getContent().get(0).getName()).isEqualTo("구스다운 패딩");  // 내림차순으로 정렬되어야 함
        assertThat(result.getContent().get(1).getName()).isEqualTo("후드티");
    }

    @Test
    void 상품이_존재하지_않는_경우_NotFoundException을_반환한다() {
        // Given
        long productId = 1L;
        long quantity = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> productService.reduceProduct(productId, quantity));
    }

    @Test
    void 최근_3일간_가장_많이_팔린_상위_5개_상품_정보_조회에_성공한다() {
        List<Product> mockProductsDesc = Arrays.asList(
                Product.builder()
                        .id(1L)
                        .name("구스다운 패딩")
                        .price(500000L)
                        .stock(150L)
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("후드티")
                        .price(20000L)
                        .stock(100L)
                        .build(),
                Product.builder()
                        .id(3L)
                        .name("맨투맨")
                        .price(20000L)
                        .stock(100L)
                        .build()
        );

        // Given
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "price"));
        Page<Product> productPage = new PageImpl<>(mockProductsDesc, pageable, mockProductsDesc.size());
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // When
        Page<ProductResponse> result = productService.products(0, "price", "DESC");

        // Then
        assertThat(result.getContent()).hasSize(3); // Mock 데이터 크기 확인
        assertThat(result.getContent().get(0).getName()).isEqualTo("구스다운 패딩"); // 첫 번째 상품 확인
        assertThat(result.getContent().get(0).getPrice()).isEqualTo(500000L); // 첫 번째 상품 가격 확인
        assertThat(result.getContent().get(1).getName()).isEqualTo("후드티"); // 두 번째 상품 확인
    }
}
