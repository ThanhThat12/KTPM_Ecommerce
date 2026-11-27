package com.example.ecommerce.analytics.dto;

import java.math.BigDecimal;

public record TopProductResponse(
        String productId,
        String productName,
        long totalUnitsSold,
        BigDecimal totalRevenue
) {
}
