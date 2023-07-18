package firework.controller;

import firework.model.product.get.ProductSearchEntry;
import firework.model.product.get.ProductsSearchData;
import firework.service.FwProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Products Controller")
@RestController
@RequestMapping("/v1/bus/{businessId}/store/{businessStoreId}/products")
@RequiredArgsConstructor
public class ProductController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_CURRENT_PAGE = "1";

    private final FwProductService fwProductService;

    @GetMapping
    @Operation(operationId = "getProductsForStore", summary = "Get Products for Store")
    @ApiResponse(content = @Content(mediaType = APPLICATION_JSON_VALUE))
    public ResponseEntity<ProductsSearchData> getProductsForStore(@PathVariable(value = "businessId") String businessId,
                                                                  @PathVariable(value = "businessStoreId") String businessStoreId,
                                                                  @RequestParam("timestamp") int timestamp,
                                                                  @RequestParam(value = "page", defaultValue = DEFAULT_CURRENT_PAGE, required = false) @Positive int page,
                                                                  @RequestParam(value = "per_page", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                                  @RequestHeader("X-fw-hmac-sha512") String hmacEncoded,
                                                                  @RequestHeader("X-fw-timestamp") String headerTimestamp
    ) {
        final var productsData = fwProductService.getProductsForStore(businessId, businessStoreId, page, pageSize);
        return ResponseEntity.ok(productsData);
    }

    @GetMapping
    @RequestMapping("/{productExtId}")
    @Operation(operationId = "getProductForStoreByProductExtId", summary = " Get Product from Store By Id")
    @ApiResponse(content = @Content(mediaType = APPLICATION_JSON_VALUE))
    public ResponseEntity<ProductSearchEntry> getProductForStoreByProductExtId(@PathVariable(value = "businessId") String businessId,
                                                                               @PathVariable(value = "businessStoreId") String businessStoreId,
                                                                               @RequestParam("timestamp") int timestamp,
                                                                               @PathVariable(value = "productExtId") String productExtId,
                                                                               @RequestHeader("X-fw-hmac-sha512") String hmacEncoded,
                                                                               @RequestHeader("X-fw-timestamp") String headerTimestamp) {
        final var productData = fwProductService.getProductForStoreByProductExtId(productExtId, businessStoreId);
        return ResponseEntity.ok(productData);
    }

}
