package com.example.ecommerce.media.dto;

import com.example.ecommerce.media.model.MediaAsset;

import java.time.Instant;

public record MediaResponse(
        String id,
        String originalFilename,
        String storedFilename,
        String contentType,
        long sizeBytes,
        String url,
        Instant createdAt
) {
    public static MediaResponse from(MediaAsset asset) {
        return new MediaResponse(
                asset.getId(),
                asset.getOriginalFilename(),
                asset.getStoredFilename(),
                asset.getContentType(),
                asset.getSizeBytes(),
                asset.getUrl(),
                asset.getCreatedAt()
        );
    }
}
