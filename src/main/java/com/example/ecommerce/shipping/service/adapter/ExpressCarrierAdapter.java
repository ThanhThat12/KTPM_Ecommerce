package com.example.ecommerce.shipping.service.adapter;

import com.example.ecommerce.shipping.dto.ShippingQuoteResponse;
import com.example.ecommerce.shipping.model.ShippingCarrier;
import com.example.ecommerce.shipping.service.strategy.ExpressShippingStrategy;
import com.example.ecommerce.shipping.service.strategy.ShippingFeeStrategy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExpressCarrierAdapter implements ShippingCarrierAdapter {

    private final ShippingFeeStrategy strategy = new ExpressShippingStrategy();

    @Override
    public ShippingCarrier carrier() {
        return ShippingCarrier.EXPRESS;
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
        return "EXP-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}
