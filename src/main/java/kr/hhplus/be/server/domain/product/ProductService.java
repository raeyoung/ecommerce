package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.infra.product.ProductRepository;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 상품 조회
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
}
