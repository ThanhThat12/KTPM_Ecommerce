package com.example.ecommerce.shipping.service;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.shipping.dto.CreateShipmentRequest;
import com.example.ecommerce.shipping.dto.ShipmentResponse;
import com.example.ecommerce.shipping.dto.ShippingQuoteResponse;
import com.example.ecommerce.shipping.dto.UpdateTrackingRequest;
import com.example.ecommerce.shipping.model.Shipment;
import com.example.ecommerce.shipping.model.ShipmentStatus;
import com.example.ecommerce.shipping.model.TrackingEvent;
import com.example.ecommerce.shipping.repository.ShipmentRepository;
import com.example.ecommerce.shipping.service.adapter.ShippingCarrierAdapter;
import com.example.ecommerce.shipping.service.factory.ShippingCarrierFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShipmentRepository shipmentRepository;
    private final ShippingCarrierFactory carrierFactory;
    private final OrderService orderService;

    public List<ShippingQuoteResponse> quoteAllCarriers(String destinationCity, double weightKg) {
        return carrierFactory.getAllAdapters().stream()
                .map(adapter -> adapter.quote(destinationCity, weightKg))
                .toList();
    }

    public ShipmentResponse createShipment(CreateShipmentRequest request) {
        if (request == null || request.orderId() == null || request.carrier() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Order ID and shipping carrier are required");
        }
        orderService.getEntityById(request.orderId());

        ShippingCarrierAdapter adapter = carrierFactory.getAdapter(request.carrier());
        ShippingQuoteResponse quote = adapter.quote(request.destinationCity(), request.weightKg());
        String trackingCode = adapter.createWaybill(request.orderId(), request.destinationCity(), request.weightKg());

        List<TrackingEvent> history = new ArrayList<>();
        history.add(TrackingEvent.builder()
                .status(ShipmentStatus.CREATED)
                .location("Fulfillment Warehouse")
                .description("Shipment waybill created with " + request.carrier())
                .timestamp(Instant.now())
                .build());

        Shipment shipment = Shipment.builder()
                .orderId(request.orderId())
                .trackingCode(trackingCode)
                .carrier(request.carrier())
                .destinationCity(request.destinationCity())
                .weightKg(request.weightKg())
                .shippingFee(quote.fee())
                .estimatedDays(quote.estimatedDays())
                .status(ShipmentStatus.CREATED)
                .history(history)
                .build();

        return ShipmentResponse.from(shipmentRepository.save(shipment));
    }

    public ShipmentResponse updateTracking(String trackingCode, UpdateTrackingRequest request) {
        if (request == null || request.status() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Shipment status is required");
        }
        Shipment shipment = shipmentRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Shipment not found"));

        shipment.setStatus(request.status());
        shipment.getHistory().add(TrackingEvent.builder()
                .status(request.status())
                .location(request.location())
                .description(request.description())
                .timestamp(Instant.now())
                .build());

        return ShipmentResponse.from(shipmentRepository.save(shipment));
    }

    public ShipmentResponse trackByCode(String trackingCode) {
        return shipmentRepository.findByTrackingCode(trackingCode)
                .map(ShipmentResponse::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Shipment not found"));
    }

    public ShipmentResponse getByOrderId(String orderId) {
        return shipmentRepository.findByOrderId(orderId)
                .map(ShipmentResponse::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Shipment not found for order"));
    }
}
