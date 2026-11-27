package com.example.ecommerce.product.service;

import com.example.ecommerce.common.event.ReviewCreatedEvent;
import com.example.ecommerce.product.model.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Observer Pattern subscriber: recalculates product average rating when a new review arrives.
 */
@Component
@RequiredArgsConstructor
public class ProductRatingListener {

    private final ProductRepository productRepository;

    @EventListener
    public void onReviewCreated(ReviewCreatedEvent event) {
        productRepository.findById(event.productId()).ifPresent(product -> {
            int oldCount = product.getReviewCount();
            double totalScore = product.getAverageRating() * oldCount + event.rating();
            int newCount = oldCount + 1;
            double roundedAvg = Math.round((totalScore / newCount) * 100.0) / 100.0;
            product.setReviewCount(newCount);
            product.setAverageRating(roundedAvg);
            productRepository.save(product);
        });
    }
}
