package com.example.ecommerce.flashsale.repository;

import com.example.ecommerce.flashsale.model.FlashSale;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface FlashSaleRepository extends MongoRepository<FlashSale, String> {

    List<FlashSale> findByActiveTrue();

    List<FlashSale> findByActiveTrueAndEndTimeBefore(Instant now);

    List<FlashSale> findByProductIdAndActiveTrue(String productId);
}
