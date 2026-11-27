package com.example.ecommerce.cart.dto;

import com.example.ecommerce.cart.model.DiscountType;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateVoucherRequest(
        String code,
        DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal minOrderAmount,
        BigDecimal maxDiscountAmount,
        Instant expiresAt
) {
}
