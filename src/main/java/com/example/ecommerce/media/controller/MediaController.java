package com.example.ecommerce.media.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.common.security.AuthenticatedUser;
import com.example.ecommerce.media.dto.MediaResponse;
import com.example.ecommerce.media.model.MediaAsset;
import com.example.ecommerce.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MediaResponse>> upload(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @RequestParam("file") MultipartFile file
    ) {
        MediaResponse response = mediaService.upload(file, currentUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("File uploaded", response));
    }

    @GetMapping("/files/{storedFilename}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String storedFilename) {
        MediaAsset asset = mediaService.getByStoredFilename(storedFilename);
        byte[] bytes = mediaService.loadBytes(storedFilename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(asset.getContentType()))
                .body(bytes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        mediaService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Media deleted", null));
    }
}
