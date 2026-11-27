package com.example.ecommerce.product.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductRequest(
        String sku,
        String name,
        String description,
        String categoryId,
        BigDecimal price,
        int stockQuantity,
        List<String> imageUrls,
        Map<String, String> attributes
) {
}
