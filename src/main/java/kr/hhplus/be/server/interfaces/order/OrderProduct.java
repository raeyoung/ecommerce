package kr.hhplus.be.server.interfaces.order;

public record OrderProduct(
        Long productId,
        Long quantity   // 주문 상품 수량
) {
}
