package com.example.ecommerce.media.repository;

import com.example.ecommerce.media.model.MediaAsset;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MediaRepository extends MongoRepository<MediaAsset, String> {

    Optional<MediaAsset> findByStoredFilename(String storedFilename);
}
