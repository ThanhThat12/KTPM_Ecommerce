package com.example.ecommerce.payment.dto;

import com.example.ecommerce.payment.model.PaymentStatus;

public record GatewayPaymentResult(
        PaymentStatus status,
        String transactionRef,
        String message
) {
}
