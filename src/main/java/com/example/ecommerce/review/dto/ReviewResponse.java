package com.example.ecommerce.review.dto;

import com.example.ecommerce.review.model.Review;

import java.time.Instant;

public record ReviewResponse(
        String id,
        String productId,
        String userId,
        String userFullName,
        int rating,
        String comment,
        Instant createdAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProductId(),
                review.getUserId(),
                review.getUserFullName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
