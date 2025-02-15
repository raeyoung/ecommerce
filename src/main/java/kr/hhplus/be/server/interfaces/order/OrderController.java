package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.facade.order.OrderFacade;
import kr.hhplus.be.server.global.model.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "주문 API")
public class OrderController {

    private final OrderFacade orderFacade;

    @Operation(summary = "주문 API")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    @PostMapping()
    public CommonApiResponse<OrderResponse> order(long userId, @RequestBody OrderRequest request) {
        return CommonApiResponse.success(orderFacade.createOrder(userId, request));
    }
}
