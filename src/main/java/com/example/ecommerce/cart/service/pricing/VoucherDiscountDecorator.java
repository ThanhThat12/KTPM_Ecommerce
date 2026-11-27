package com.example.ecommerce.cart.service.pricing;

import com.example.ecommerce.cart.model.Voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Decorator combining with Strategy to apply voucher discounts.
 */
public class VoucherDiscountDecorator extends PriceDecorator {

    private final Voucher voucher;
    private final DiscountStrategy discountStrategy;

    public VoucherDiscountDecorator(PriceCalculator wrapped, Voucher voucher, DiscountStrategy discountStrategy) {
        super(wrapped);
        this.voucher = voucher;
        this.discountStrategy = discountStrategy;
    }

    @Override
    public BigDecimal calculate() {
        BigDecimal current = wrapped.calculate();
        BigDecimal discount = discountStrategy.calculateDiscount(current, voucher);
        return current.subtract(discount).max(BigDecimal.ZERO);
    }

    @Override
    public List<String> describeBreakdown() {
        List<String> lines = new ArrayList<>(wrapped.describeBreakdown());
        BigDecimal current = wrapped.calculate();
        BigDecimal discount = discountStrategy.calculateDiscount(current, voucher);
        lines.add("Voucher (" + voucher.getCode() + "): -" + discount);
        return lines;
    }
}
