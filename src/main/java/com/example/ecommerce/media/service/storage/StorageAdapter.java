package com.example.ecommerce.media.service.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Adapter Pattern: abstracts underlying file storage (Local disk, Cloudinary, AWS S3).
 */
public interface StorageAdapter {

    String store(MultipartFile file, String storedFilename);

    byte[] load(String storedFilename);

    void delete(String storedFilename);
}
