package com.example.ecommerce.shipping.dto;

import com.example.ecommerce.shipping.model.ShipmentStatus;

public record UpdateTrackingRequest(
        ShipmentStatus status,
        String location,
        String description
) {
}
