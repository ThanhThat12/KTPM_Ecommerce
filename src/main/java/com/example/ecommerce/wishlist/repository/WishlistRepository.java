package com.example.ecommerce.wishlist.repository;

import com.example.ecommerce.wishlist.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {

    Optional<Wishlist> findByUserId(String userId);
}
