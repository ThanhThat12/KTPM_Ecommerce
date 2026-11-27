package com.example.ecommerce.flashsale.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateFlashSaleRequest(
        String productId,
        BigDecimal salePrice,
        int maxQuantity,
        Instant startTime,
        Instant endTime
) {
}
