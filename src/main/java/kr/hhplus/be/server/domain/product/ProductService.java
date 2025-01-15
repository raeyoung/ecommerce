package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.global.exception.ExceptionMessage;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 상품 목록 조회
     * price 내림차순으로 5개 상품씩 조회한다.
     * @param page
     * @param criteria
     * @param sort
     * @return
     */
    public Page<ProductResponse> products(int page, String criteria, String sort) {
        Pageable pageable = (sort.equals("ASC")) ?
                PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC, criteria))
                : PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, criteria));

        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductResponse::from);
    }

    /**
     * 단일 상품 조회
     * @param productId
     * @return
     */
    @Cacheable(cacheNames = "products", key = "#productId")
    public Optional<Product> product(long productId) {
        return productRepository.findById(productId);
    }

    /**
     * 상품 수량 감소
     * @param productId
     * @param quantity
     * @return
     */
    @CachePut(cacheNames = "products", key = "#productId")
    public Product reduceProduct(long productId, long quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessage.PRODUCT_NOT_FOUND.getMessage()));

        product.reduceStock(quantity);

        productRepository.save(product);

        return product;
    }
}
