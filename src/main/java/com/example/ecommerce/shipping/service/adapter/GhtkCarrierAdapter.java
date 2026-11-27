package com.example.ecommerce.shipping.service.adapter;

import com.example.ecommerce.shipping.dto.ShippingQuoteResponse;
import com.example.ecommerce.shipping.model.ShippingCarrier;
import com.example.ecommerce.shipping.service.strategy.ShippingFeeStrategy;
import com.example.ecommerce.shipping.service.strategy.StandardShippingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class GhtkCarrierAdapter implements ShippingCarrierAdapter {

    private final ShippingFeeStrategy strategy = new StandardShippingStrategy();

    @Override
    public ShippingCarrier carrier() {
        return ShippingCarrier.GHTK;
    }

    @Override
    public ShippingQuoteResponse quote(String destinationCity, double weightKg) {
        BigDecimal discounted = strategy.calculateFee(destinationCity, weightKg)
                .subtract(new BigDecimal("3000"))
                .max(new BigDecimal("18000"));
        return new ShippingQuoteResponse(carrier(), discounted, strategy.estimatedDays(destinationCity) + 1);
    }

    @Override
    public String createWaybill(String orderId, String destinationCity, double weightKg) {
        return "GHTK-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}
