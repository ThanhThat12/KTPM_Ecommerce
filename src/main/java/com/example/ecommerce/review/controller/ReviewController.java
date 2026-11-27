package com.example.ecommerce.review.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.common.security.AuthenticatedUser;
import com.example.ecommerce.review.dto.CreateReviewRequest;
import com.example.ecommerce.review.dto.ReviewResponse;
import com.example.ecommerce.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @RequestBody CreateReviewRequest request
    ) {
        ReviewResponse response = reviewService.createReview(currentUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Review submitted", response));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getProductReviews(@PathVariable String productId) {
        return ResponseEntity.ok(ApiResponse.ok(reviewService.getByProductId(productId)));
    }
}
