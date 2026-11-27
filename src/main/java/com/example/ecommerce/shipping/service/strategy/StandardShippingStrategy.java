package com.example.ecommerce.shipping.service.strategy;

import java.math.BigDecimal;

public class StandardShippingStrategy implements ShippingFeeStrategy {

    private static final BigDecimal BASE_FEE = new BigDecimal("25000");
    private static final BigDecimal PER_KG_FEE = new BigDecimal("8000");

    @Override
    public BigDecimal calculateFee(String destinationCity, double weightKg) {
        double billableKg = Math.max(1.0, Math.ceil(weightKg));
        return BASE_FEE.add(PER_KG_FEE.multiply(BigDecimal.valueOf(billableKg - 1)));
    }

    @Override
    public int estimatedDays(String destinationCity) {
        return isMajorCity(destinationCity) ? 2 : 4;
    }

    private boolean isMajorCity(String city) {
        if (city == null) {
            return false;
        }
        String lower = city.toLowerCase();
        return lower.contains("ho chi minh") || lower.contains("hcm") || lower.contains("ha noi") || lower.contains("hanoi");
    }
}
