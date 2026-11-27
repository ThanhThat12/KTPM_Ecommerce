package com.example.ecommerce.cart.service.pricing;

import com.example.ecommerce.cart.model.Voucher;

import java.math.BigDecimal;

public class FixedAmountDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal calculateDiscount(BigDecimal amount, Voucher voucher) {
        return voucher.getDiscountValue().min(amount);
    }
}
