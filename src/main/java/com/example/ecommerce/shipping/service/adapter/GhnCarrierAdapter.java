package com.example.ecommerce.shipping.service.adapter;

import com.example.ecommerce.shipping.dto.ShippingQuoteResponse;
import com.example.ecommerce.shipping.model.ShippingCarrier;
import com.example.ecommerce.shipping.service.strategy.ShippingFeeStrategy;
import com.example.ecommerce.shipping.service.strategy.StandardShippingStrategy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GhnCarrierAdapter implements ShippingCarrierAdapter {

    private final ShippingFeeStrategy strategy = new StandardShippingStrategy();

    @Override
    public ShippingCarrier carrier() {
        return ShippingCarrier.GHN;
    }

    @Override
    public ShippingQuoteResponse quote(String destinationCity, double weightKg) {
        return new ShippingQuoteResponse(
                carrier(),
                strategy.calculateFee(destinationCity, weightKg),
                strategy.estimatedDays(destinationCity)
        );
    }

    @Override
    public String createWaybill(String orderId, String destinationCity, double weightKg) {
        return "GHN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}
