package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.interfaces.product.ProductRequest;

import java.util.List;

public record OrderRequest (
        Long userId,
        List<ProductRequest> productList
){}