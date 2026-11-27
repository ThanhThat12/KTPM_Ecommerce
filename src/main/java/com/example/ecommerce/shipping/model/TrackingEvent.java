package com.example.ecommerce.shipping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEvent {
    private ShipmentStatus status;
    private String location;
    private String description;
    private Instant timestamp;
}
