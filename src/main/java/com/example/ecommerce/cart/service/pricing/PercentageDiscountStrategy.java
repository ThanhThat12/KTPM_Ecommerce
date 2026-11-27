package com.example.ecommerce.cart.service.pricing;

import com.example.ecommerce.cart.model.Voucher;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscount(BigDecimal amount, Voucher voucher) {
        BigDecimal discount = amount
                .multiply(voucher.getDiscountValue())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        if (voucher.getMaxDiscountAmount() != null
                && discount.compareTo(voucher.getMaxDiscountAmount()) > 0) {
            return voucher.getMaxDiscountAmount();
        }
        return discount.min(amount);
    }
}
