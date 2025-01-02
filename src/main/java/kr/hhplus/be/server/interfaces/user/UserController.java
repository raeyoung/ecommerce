package kr.hhplus.be.server.interfaces.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "사용자 API")
public class UserController {

    @Operation(summary = "잔액 조회 API")
    @Parameter(name = "userId", description = "사용자 고유 ID")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserBalanceResponse.class)))
    @GetMapping("/balance/{userId}")
    public ResponseEntity<UserBalanceResponse> balance(@PathVariable long userId) {

        return ResponseEntity.ok(new UserBalanceResponse(1L, 1000000L));
    }

    @Operation(summary = "잔액 충전 API")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserBalanceResponse.class)))
    @PatchMapping("/balance/charge")
    public ResponseEntity<UserBalanceResponse> charge(@RequestBody UserBalanceRequest request) {

        return ResponseEntity.ok(new UserBalanceResponse(request.userId(), 1000000L + request.amount()));
    }
}
