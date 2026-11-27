package com.example.ecommerce.common.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Domain event published when an order is placed.
 * Modules such as product (inventory deduction) and notification (email/in-app alert)
 * can subscribe via Spring's {@code @EventListener} (Observer Pattern).
 */
public record OrderPlacedEvent(
        String orderId,
        String userId,
        String userEmail,
        BigDecimal totalAmount,
        List<OrderItemSnapshot> items,
        Instant occurredAt
) {
    public record OrderItemSnapshot(String productId, int quantity) {
    }
}
