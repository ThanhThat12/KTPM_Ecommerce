package com.example.ecommerce.search.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.product.dto.ProductResponse;
import com.example.ecommerce.search.dto.PagedResponse;
import com.example.ecommerce.search.dto.ProductSearchCriteria;
import com.example.ecommerce.search.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Boolean inStockOnly,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        ProductSearchCriteria criteria = new ProductSearchCriteria(
                keyword, categoryId, minPrice, maxPrice, minRating, inStockOnly,
                sortBy, sortDirection, page, size
        );
        return ResponseEntity.ok(ApiResponse.ok(productSearchService.search(criteria)));
    }
}
