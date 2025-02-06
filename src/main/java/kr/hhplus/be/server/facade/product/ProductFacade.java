package kr.hhplus.be.server.facade.product;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductFacade {

    private final ProductService productService;
    private final OrderService orderService;

    public ProductFacade(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    public Page<ProductResponse> getProducts(int page, String criteria, String sort) {
        return productService.getProducts(page, criteria, sort);
    }

    public List<ProductResponse> popularProducts() {
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = endDateTime.minusDays(3);

        return orderService.getPopularProducts(startDateTime, endDateTime)
                .stream()
                .map(productId -> productService.getProduct(productId)
                        .orElse(Product.notAvailable(productId)))
                .map(ProductResponse::from)
                .toList();
    }
}
