package com.example.ecommerce.cart.service.pricing;

import com.example.ecommerce.cart.model.Voucher;

import java.math.BigDecimal;

/**
 * Strategy Pattern: defines interchangeable discount calculation algorithms.
 */
public interface DiscountStrategy {

    BigDecimal calculateDiscount(BigDecimal amount, Voucher voucher);
}
