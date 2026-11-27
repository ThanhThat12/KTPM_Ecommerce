package com.example.ecommerce.cart.service.pricing;

import java.math.BigDecimal;
import java.util.List;

/**
 * Decorator Pattern Component: computes total price and records each adjustment layer.
 */
public interface PriceCalculator {

    BigDecimal calculate();

    List<String> describeBreakdown();
}
