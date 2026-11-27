package com.example.ecommerce.cart.service.pricing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BasePriceCalculator implements PriceCalculator {

    private final BigDecimal subtotal;

    public BasePriceCalculator(BigDecimal subtotal) {
        this.subtotal = subtotal != null ? subtotal : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculate() {
        return subtotal;
    }

    @Override
    public List<String> describeBreakdown() {
        List<String> lines = new ArrayList<>();
        lines.add("Subtotal: " + subtotal);
        return lines;
    }
}
