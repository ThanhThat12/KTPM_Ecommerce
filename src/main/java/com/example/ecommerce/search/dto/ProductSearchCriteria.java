package com.example.ecommerce.search.dto;

import java.math.BigDecimal;

public record ProductSearchCriteria(
        String keyword,
        String categoryId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Double minRating,
        Boolean inStockOnly,
        String sortBy,
        String sortDirection,
        int page,
        int size
) {
}
