package com.example.ecommerce.shipping.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.shipping.dto.CreateShipmentRequest;
import com.example.ecommerce.shipping.dto.ShipmentResponse;
import com.example.ecommerce.shipping.dto.ShippingQuoteResponse;
import com.example.ecommerce.shipping.dto.UpdateTrackingRequest;
import com.example.ecommerce.shipping.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @GetMapping("/quotes")
    public ResponseEntity<ApiResponse<List<ShippingQuoteResponse>>> getQuotes(
            @RequestParam String destinationCity,
            @RequestParam(defaultValue = "1.0") double weightKg
    ) {
        return ResponseEntity.ok(ApiResponse.ok(shippingService.quoteAllCarriers(destinationCity, weightKg)));
    }

    @PostMapping("/shipments")
    public ResponseEntity<ApiResponse<ShipmentResponse>> createShipment(@RequestBody CreateShipmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Shipment created", shippingService.createShipment(request)));
    }

    @PatchMapping("/shipments/{trackingCode}/tracking")
    public ResponseEntity<ApiResponse<ShipmentResponse>> updateTracking(
            @PathVariable String trackingCode,
            @RequestBody UpdateTrackingRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Tracking updated",
                shippingService.updateTracking(trackingCode, request)));
    }

    @GetMapping("/shipments/track/{trackingCode}")
    public ResponseEntity<ApiResponse<ShipmentResponse>> track(@PathVariable String trackingCode) {
        return ResponseEntity.ok(ApiResponse.ok(shippingService.trackByCode(trackingCode)));
    }

    @GetMapping("/shipments/orders/{orderId}")
    public ResponseEntity<ApiResponse<ShipmentResponse>> getByOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponse.ok(shippingService.getByOrderId(orderId)));
    }
}
