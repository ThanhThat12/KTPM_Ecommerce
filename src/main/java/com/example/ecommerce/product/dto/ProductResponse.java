package com.example.ecommerce.product.dto;

import com.example.ecommerce.product.model.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ProductResponse(
        String id,
        String sku,
        String name,
        String description,
        String categoryId,
        BigDecimal price,
        int stockQuantity,
        List<String> imageUrls,
        Map<String, String> attributes,
        boolean active,
        Instant createdAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCategoryId(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrls(),
                product.getAttributes(),
                product.isActive(),
                product.getCreatedAt()
        );
    }
}
