package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.user.UserBalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "상품 API")
public class ProductController {

    @Operation(summary = "상품조회 API")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserBalanceResponse.class)))
    @GetMapping()
    public ResponseEntity<List<ProductResponse>> products() {
        List<ProductResponse> list = new ArrayList<>();

        list.add(ProductResponse.of(1L, "후드티", 20000, 100));
        list.add(ProductResponse.of(2L, "구스타운 패딩", 500000, 150));
        list.add(ProductResponse.of(3L, "맥코트", 600000, 100));
        list.add(ProductResponse.of(4L, "코듀로이 팬츠", 35000, 100));
        list.add(ProductResponse.of(5L, "울양말", 15000, 50));
        list.add(ProductResponse.of(6L, "맨투맨", 38000, 30));

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "상위 상품 조회 API")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))))
    @GetMapping("/top")
    public ResponseEntity<List<ProductResponse>> topProducts() {
        List<ProductResponse> list = new ArrayList<>();

        list.add(ProductResponse.of(1L, "후드티", 20000, 100));
        list.add(ProductResponse.of(2L, "구스타운 패딩", 500000, 150));
        list.add(ProductResponse.of(3L, "맥코트", 600000, 100));
        list.add(ProductResponse.of(4L, "코듀로이 팬츠", 35000, 100));
        list.add(ProductResponse.of(5L, "울양말", 15000, 50));

        return ResponseEntity.ok(list);
    }
}
