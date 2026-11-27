package com.example.ecommerce.shipping.model;

import com.example.ecommerce.common.model.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shipments")
public class Shipment extends BaseDocument {

    private String orderId;

    @Indexed(unique = true)
    private String trackingCode;

    private ShippingCarrier carrier;

    private String destinationCity;

    private double weightKg;

    private BigDecimal shippingFee;

    private int estimatedDays;

    @Builder.Default
    private ShipmentStatus status = ShipmentStatus.CREATED;

    @Builder.Default
    private List<TrackingEvent> history = new ArrayList<>();
}
