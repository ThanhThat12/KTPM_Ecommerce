package com.example.ecommerce.cart.dto;

import com.example.ecommerce.cart.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public record CartSummaryResponse(
        String userId,
        List<CartItem> items,
        BigDecimal subtotal,
        BigDecimal finalTotal,
        String appliedVoucherCode,
        List<String> priceBreakdown
) {
}
