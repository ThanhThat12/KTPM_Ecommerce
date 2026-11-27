package com.example.ecommerce.shipping.repository;

import com.example.ecommerce.shipping.model.Shipment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShipmentRepository extends MongoRepository<Shipment, String> {

    Optional<Shipment> findByOrderId(String orderId);

    Optional<Shipment> findByTrackingCode(String trackingCode);
}
