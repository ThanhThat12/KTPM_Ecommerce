package com.example.ecommerce.payment.dto;

import com.example.ecommerce.payment.model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        String orderId,
        String userId,
        BigDecimal amount,
        PaymentMethod method
) {
}
