package kr.hhplus.be.server.interfaces.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.user.UserPoint;
import kr.hhplus.be.server.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "사용자 API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "잔액 조회 API")
    @Parameter(name = "userId", description = "사용자 고유 ID")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPointResponse.class)))
    @GetMapping("/point/{userId}")
    public ResponseEntity<UserPointResponse> point(@PathVariable long userId) {
        return ResponseEntity.ok(userService.point(userId));
    }

    @Operation(summary = "잔액 충전 API")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPointRequest.class)))
    @PatchMapping("/point/charge")
    public ResponseEntity<UserPointResponse> charge(@RequestBody UserPointRequest request) {

        return ResponseEntity.ok(userService.chargePoint(request));
    }
}
