package com.example.ecommerce.shipping.dto;

import com.example.ecommerce.shipping.model.ShippingCarrier;

public record CreateShipmentRequest(
        String orderId,
        ShippingCarrier carrier,
        String destinationCity,
        double weightKg
) {
}
