package kr.hhplus.be.server.facade.order;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderFacade {

    private final OrderService orderService;
    private final ProductService productService;

    public OrderFacade(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @Transactional
    public OrderResponse createOrder(long userId, OrderRequest request) {
        List<OrderItem> orderItems = request.orderProductList()
                .stream()
                .map(orderProduct -> {
                    Product product = productService.reduceProduct(orderProduct.productId(), orderProduct.quantity());
                    return OrderItem.create(orderProduct.quantity(), product);
                })
                .toList();
        return OrderResponse.to(orderService.createOrder(userId, orderItems));
    }
}
