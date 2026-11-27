package com.example.ecommerce.cart.service.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Decorator applying an additional 5% VIP member discount.
 */
public class VipDiscountDecorator extends PriceDecorator {

    private static final BigDecimal VIP_RATE = new BigDecimal("0.05");

    public VipDiscountDecorator(PriceCalculator wrapped) {
        super(wrapped);
    }

    @Override
    public BigDecimal calculate() {
        BigDecimal current = wrapped.calculate();
        BigDecimal discount = current.multiply(VIP_RATE).setScale(2, RoundingMode.HALF_UP);
        return current.subtract(discount).max(BigDecimal.ZERO);
    }

    @Override
    public List<String> describeBreakdown() {
        List<String> lines = new ArrayList<>(wrapped.describeBreakdown());
        BigDecimal current = wrapped.calculate();
        BigDecimal discount = current.multiply(VIP_RATE).setScale(2, RoundingMode.HALF_UP);
        lines.add("VIP Member Discount (5%): -" + discount);
        return lines;
    }
}
