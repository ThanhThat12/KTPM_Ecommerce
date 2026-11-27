package com.example.ecommerce.review.service;

import com.example.ecommerce.common.event.ReviewCreatedEvent;
import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.order.model.OrderStatus;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.product.service.ProductService;
import com.example.ecommerce.review.dto.CreateReviewRequest;
import com.example.ecommerce.review.dto.ReviewResponse;
import com.example.ecommerce.review.model.Review;
import com.example.ecommerce.review.repository.ReviewRepository;
import com.example.ecommerce.user.model.User;
import com.example.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    public ReviewResponse createReview(String userId, CreateReviewRequest request) {
        if (request == null || request.productId() == null || request.rating() < 1 || request.rating() > 5) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Product ID and rating (1-5) are required");
        }

        // Verify product exists
        productService.getEntityById(request.productId());

        if (reviewRepository.existsByUserIdAndProductId(userId, request.productId())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "You have already reviewed this product");
        }

        // Only verified buyers with DELIVERED orders can review
        boolean hasDeliveredOrder = orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .anyMatch(item -> item.getProductId().equals(request.productId()));

        if (!hasDeliveredOrder) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Only customers who received this product can review it");
        }

        User user = userService.getEntityById(userId);
        Review review = Review.builder()
                .productId(request.productId())
                .userId(userId)
                .userFullName(user.getFullName())
                .rating(request.rating())
                .comment(request.comment())
                .build();

        Review saved = reviewRepository.save(review);
        eventPublisher.publishEvent(new ReviewCreatedEvent(
                saved.getId(),
                saved.getProductId(),
                userId,
                saved.getRating(),
                Instant.now()
        ));

        return ReviewResponse.from(saved);
    }

    public List<ReviewResponse> getByProductId(String productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(ReviewResponse::from)
                .toList();
    }
}
