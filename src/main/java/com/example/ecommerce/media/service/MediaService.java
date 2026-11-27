package com.example.ecommerce.media.service;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.media.dto.MediaResponse;
import com.example.ecommerce.media.model.MediaAsset;
import com.example.ecommerce.media.repository.MediaRepository;
import com.example.ecommerce.media.service.storage.StorageAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private final MediaRepository mediaRepository;
    private final StorageAdapter storageAdapter;

    public MediaResponse upload(MultipartFile file, String userId) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Uploaded file must not be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Only JPEG, PNG, WEBP, and GIF images are allowed");
        }

        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "upload";
        String extension = extractSafeExtension(originalName);
        String storedFilename = UUID.randomUUID() + extension;

        String url = storageAdapter.store(file, storedFilename);

        MediaAsset asset = MediaAsset.builder()
                .originalFilename(originalName)
                .storedFilename(storedFilename)
                .contentType(contentType)
                .sizeBytes(file.getSize())
                .url(url)
                .uploadedByUserId(userId)
                .build();

        return MediaResponse.from(mediaRepository.save(asset));
    }

    public MediaAsset getByStoredFilename(String storedFilename) {
        return mediaRepository.findByStoredFilename(storedFilename)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Media asset not found"));
    }

    public byte[] loadBytes(String storedFilename) {
        return storageAdapter.load(storedFilename);
    }

    public void delete(String mediaId) {
        MediaAsset asset = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Media asset not found"));
        storageAdapter.delete(asset.getStoredFilename());
        mediaRepository.delete(asset);
    }

    private String extractSafeExtension(String originalName) {
        int dot = originalName.lastIndexOf('.');
        if (dot < 0 || dot == originalName.length() - 1) {
            return "";
        }
        String ext = originalName.substring(dot).toLowerCase().replaceAll("[^a-z0-9.]", "");
        return ext.length() <= 6 ? ext : "";
    }
}
