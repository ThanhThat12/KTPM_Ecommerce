package com.example.ecommerce.common.event;

import java.time.Instant;
import java.util.List;

/**
 * Domain event published when an order is cancelled.
 * Product module listens to restore stock; Notification module listens to alert the user.
 */
public record OrderCancelledEvent(
        String orderId,
        String userId,
        String userEmail,
        String reason,
        List<OrderPlacedEvent.OrderItemSnapshot> items,
        Instant occurredAt
) {
}
