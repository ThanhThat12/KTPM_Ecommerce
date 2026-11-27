package com.example.ecommerce.cart.service.pricing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Decorator adding shipping fee (free when subtotal reaches the threshold).
 */
public class ShippingFeeDecorator extends PriceDecorator {

    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("500000");
    private static final BigDecimal STANDARD_SHIPPING_FEE = new BigDecimal("30000");

    public ShippingFeeDecorator(PriceCalculator wrapped) {
        super(wrapped);
    }

    @Override
    public BigDecimal calculate() {
        BigDecimal current = wrapped.calculate();
        return current.add(resolveShippingFee(current));
    }

    @Override
    public List<String> describeBreakdown() {
        List<String> lines = new ArrayList<>(wrapped.describeBreakdown());
        BigDecimal current = wrapped.calculate();
        BigDecimal fee = resolveShippingFee(current);
        lines.add(fee.compareTo(BigDecimal.ZERO) == 0 ? "Shipping Fee: FREE" : "Shipping Fee: +" + fee);
        return lines;
    }

    private BigDecimal resolveShippingFee(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        return STANDARD_SHIPPING_FEE;
    }
}
