package kr.hhplus.be.server.interfaces.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "쿠폰 API")
public class CouponController {

    @Operation(summary = "선착순 쿠폰 발급 API")
    @Parameter(name = "userId", description = "사용자 고유 ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/issue/{userId}")
    public ResponseEntity<String> issue(@PathVariable long userId) {

        return ResponseEntity.ok("새해맞이 할인 쿠폰이 발급되었습니다.");
    }

    @Operation(summary = "선착순 쿠폰 조회 API")
    @Parameter(name = "userId", description = "사용자 고유 ID")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    @GetMapping("/{userId}")
    public ResponseEntity<CouponResponse> coupons(@PathVariable long userId) {

        return ResponseEntity.ok(new CouponResponse(1L, "새해맞이 할인 쿠폰", "새해를 맞이하여 선착순 할인쿠폰 제공", 2000, "available"));
    }
}
