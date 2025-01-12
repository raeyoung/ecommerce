package kr.hhplus.be.server.interfaces.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.facade.coupon.CouponFacade;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "쿠폰 API")
public class CouponController {

    private final CouponFacade couponFacade;

    @Operation(summary = "선착순 쿠폰 발급 API")
    @Parameter(name = "userId", description = "사용자 고유 ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/issue/{userId}")
    public ResponseEntity<String> issueCoupon(CouponRequest request) {
        String message = couponFacade.issueCoupon(request);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "쿠폰 목록 조회 API")
    @Parameter(name = "userId", description = "사용자 고유 ID")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class)))
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<IssuedCouponResponse>> coupons(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<IssuedCouponResponse> coupons = couponFacade.getIssuedCoupons(userId, page, size);
        return ResponseEntity.ok(coupons);
    }
}
