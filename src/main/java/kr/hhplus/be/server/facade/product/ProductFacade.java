package kr.hhplus.be.server.facade.product;

import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ProductFacade {

    private final ProductService productService;

    public ProductFacade(ProductService productService) {
        this.productService = productService;
    }

    public Page<ProductResponse> products(int page, String criteria, String sort) {
        return productService.products(page, criteria, sort);
    }
}
