package com.example.ecommerce.payment.dto;

import com.example.ecommerce.payment.model.Payment;
import com.example.ecommerce.payment.model.PaymentMethod;
import com.example.ecommerce.payment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        String id,
        String orderId,
        String userId,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        String transactionRef,
        String gatewayMessage,
        Instant createdAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getTransactionRef(),
                payment.getGatewayMessage(),
                payment.getCreatedAt()
        );
    }
}
