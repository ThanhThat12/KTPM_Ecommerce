package com.example.ecommerce.notification.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.notification.dto.NotificationRequest;
import com.example.ecommerce.notification.dto.NotificationResponse;
import com.example.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> send(@RequestBody NotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Notification sent", notificationService.send(request)));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getByUserId(userId)));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok("Marked as read", notificationService.markAsRead(id)));
    }
}
