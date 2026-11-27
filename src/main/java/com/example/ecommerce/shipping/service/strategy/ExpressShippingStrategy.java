package com.example.ecommerce.shipping.service.strategy;

import java.math.BigDecimal;

public class ExpressShippingStrategy implements ShippingFeeStrategy {

    private static final BigDecimal BASE_FEE = new BigDecimal("45000");
    private static final BigDecimal PER_KG_FEE = new BigDecimal("15000");

    @Override
    public BigDecimal calculateFee(String destinationCity, double weightKg) {
        double billableKg = Math.max(1.0, Math.ceil(weightKg));
        return BASE_FEE.add(PER_KG_FEE.multiply(BigDecimal.valueOf(billableKg - 1)));
    }

    @Override
    public int estimatedDays(String destinationCity) {
        return 1;
    }
}
