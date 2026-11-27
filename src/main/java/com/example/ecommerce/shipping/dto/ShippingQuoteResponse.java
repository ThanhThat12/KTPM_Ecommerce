package com.example.ecommerce.shipping.dto;

import com.example.ecommerce.shipping.model.ShippingCarrier;

import java.math.BigDecimal;

public record ShippingQuoteResponse(
        ShippingCarrier carrier,
        BigDecimal fee,
        int estimatedDays
) {
}
