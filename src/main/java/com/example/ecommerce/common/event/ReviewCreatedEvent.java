package com.example.ecommerce.common.event;

import java.time.Instant;

/**
 * Domain event published when a product review is created.
 * Product module subscribes via Observer pattern to recalculate average rating.
 */
public record ReviewCreatedEvent(
        String reviewId,
        String productId,
        String userId,
        int rating,
        Instant occurredAt
) {
}
