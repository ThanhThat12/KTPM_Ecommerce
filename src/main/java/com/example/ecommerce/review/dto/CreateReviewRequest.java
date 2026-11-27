package com.example.ecommerce.review.dto;

public record CreateReviewRequest(
        String productId,
        int rating,
        String comment
) {
}
