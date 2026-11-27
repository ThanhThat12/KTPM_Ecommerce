package com.example.ecommerce.shipping.service.strategy;

import java.math.BigDecimal;

/**
 * Strategy Pattern: calculates shipping cost and ETA based on delivery tier.
 */
public interface ShippingFeeStrategy {

    BigDecimal calculateFee(String destinationCity, double weightKg);

    int estimatedDays(String destinationCity);
}
