package com.example.ecommerce.flashsale.dto;

import com.example.ecommerce.flashsale.model.FlashSale;

import java.math.BigDecimal;
import java.time.Instant;

public record FlashSaleResponse(
        String id,
        String productId,
        String productName,
        BigDecimal originalPrice,
        BigDecimal salePrice,
        int maxQuantity,
        int soldQuantity,
        int remainingQuantity,
        Instant startTime,
        Instant endTime,
        boolean runningNow
) {
    public static FlashSaleResponse from(FlashSale sale) {
        return new FlashSaleResponse(
                sale.getId(),
                sale.getProductId(),
                sale.getProductName(),
                sale.getOriginalPrice(),
                sale.getSalePrice(),
                sale.getMaxQuantity(),
                sale.getSoldQuantity(),
                Math.max(0, sale.getMaxQuantity() - sale.getSoldQuantity()),
                sale.getStartTime(),
                sale.getEndTime(),
                sale.isRunningNow()
        );
    }
}
