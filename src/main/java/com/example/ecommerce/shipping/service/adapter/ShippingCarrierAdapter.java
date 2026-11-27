package com.example.ecommerce.shipping.service.adapter;

import com.example.ecommerce.shipping.dto.ShippingQuoteResponse;
import com.example.ecommerce.shipping.model.ShippingCarrier;

/**
 * Adapter Pattern: standardizes integration with third-party logistics providers (GHN, GHTK, Express).
 */
public interface ShippingCarrierAdapter {

    ShippingCarrier carrier();

    ShippingQuoteResponse quote(String destinationCity, double weightKg);

    String createWaybill(String orderId, String destinationCity, double weightKg);
}
