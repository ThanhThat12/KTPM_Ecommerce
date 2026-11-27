package com.example.ecommerce.notification.dto;

import com.example.ecommerce.notification.model.NotificationChannel;

public record NotificationRequest(
        String userId,
        String recipient,
        NotificationChannel channel,
        String title,
        String content
) {
}
