package com.example.ecommerce.cart.repository;

import com.example.ecommerce.cart.model.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VoucherRepository extends MongoRepository<Voucher, String> {

    Optional<Voucher> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}
