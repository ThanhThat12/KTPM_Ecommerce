package com.example.ecommerce.media.service.storage;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class LocalFileStorageAdapter implements StorageAdapter {

    private final Path uploadRoot;

    public LocalFileStorageAdapter(@Value("${app.media.upload-dir:uploads}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Override
    public String store(MultipartFile file, String storedFilename) {
        try {
            Files.createDirectories(uploadRoot);
            Path target = resolveSafePath(storedFilename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/api/media/files/" + storedFilename;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to store uploaded file");
        }
    }

    @Override
    public byte[] load(String storedFilename) {
        try {
            Path target = resolveSafePath(storedFilename);
            if (!Files.exists(target)) {
                throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Media file not found");
            }
            return Files.readAllBytes(target);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to read media file");
        }
    }

    @Override
    public void delete(String storedFilename) {
        try {
            Path target = resolveSafePath(storedFilename);
            Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to delete media file");
        }
    }

    private Path resolveSafePath(String filename) {
        Path resolved = uploadRoot.resolve(filename).normalize();
        if (!resolved.startsWith(uploadRoot)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Invalid file path");
        }
        return resolved;
    }
}
