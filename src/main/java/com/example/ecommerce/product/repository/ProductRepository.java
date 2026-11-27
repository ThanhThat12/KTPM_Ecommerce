package com.example.ecommerce.product.repository;

import com.example.ecommerce.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    boolean existsBySku(String sku);

    List<Product> findByCategoryIdAndActiveTrue(String categoryId);

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String keyword);

    List<Product> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByActiveTrue();
}
