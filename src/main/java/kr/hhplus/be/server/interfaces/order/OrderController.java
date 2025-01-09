package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "주문 API")
public class OrderController {

    @Operation(summary = "주문 API")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    @PostMapping()
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {

        List<ProductResponse> productList = new ArrayList<>();
        productList.add(ProductResponse.of(1L, "후드티", 20000, 1));
        productList.add(ProductResponse.of(3L, "맥코트", 600000, 1));

        return ResponseEntity.ok(OrderResponse.of(1L, 6200000, "completed", productList));
    }
}
