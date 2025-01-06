package kr.hhplus.be.server.interfaces.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.facade.user.UserFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "사용자 API")
public class UserController {

    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Operation(summary = "잔액 조회 API")
    @Parameter(name = "userId", description = "사용자 고유 ID")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PointResponse.class)))
    @GetMapping("/point/{userId}")
    public ResponseEntity<PointResponse> point(@PathVariable long userId) {
        return ResponseEntity.ok(userFacade.point(userId));
    }

    @Operation(summary = "잔액 충전 API")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PointRequest.class)))
    @PatchMapping("/point/charge")
    public ResponseEntity<PointResponse> charge(@RequestBody PointRequest request) {

        return ResponseEntity.ok(userFacade.chargePoint(request));
    }
}
