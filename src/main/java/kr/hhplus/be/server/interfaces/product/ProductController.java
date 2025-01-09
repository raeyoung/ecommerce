package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.facade.product.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "상품 API")
public class ProductController {

    private final ProductFacade productFacade;

    @Operation(summary = "상품조회 API")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class)))
    @GetMapping()
    public ResponseEntity<Page<ProductResponse>> products(@RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                                          @RequestParam(required = false, defaultValue = "price", value = "orderby") String criteria,
                                                          @RequestParam(required = false, defaultValue = "DESC", value = "sort") String sort) {

        return ResponseEntity.ok(productFacade.products(page, criteria, sort));
    }

    @Operation(summary = "상위 상품 조회 API")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))))
    @GetMapping("/top")
    public ResponseEntity<List<ProductResponse>> topProducts() {
        return ResponseEntity.ok(productFacade.popularProducts());
    }
}
