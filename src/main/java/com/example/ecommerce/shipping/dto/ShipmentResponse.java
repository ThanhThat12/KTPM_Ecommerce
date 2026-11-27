package com.example.ecommerce.shipping.dto;

import com.example.ecommerce.shipping.model.Shipment;
import com.example.ecommerce.shipping.model.ShipmentStatus;
import com.example.ecommerce.shipping.model.ShippingCarrier;
import com.example.ecommerce.shipping.model.TrackingEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ShipmentResponse(
        String id,
        String orderId,
        String trackingCode,
        ShippingCarrier carrier,
        String destinationCity,
        double weightKg,
        BigDecimal shippingFee,
        int estimatedDays,
        ShipmentStatus status,
        List<TrackingEvent> history,
        Instant createdAt
) {
    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getOrderId(),
                shipment.getTrackingCode(),
                shipment.getCarrier(),
                shipment.getDestinationCity(),
                shipment.getWeightKg(),
                shipment.getShippingFee(),
                shipment.getEstimatedDays(),
                shipment.getStatus(),
                shipment.getHistory(),
                shipment.getCreatedAt()
        );
    }
}
