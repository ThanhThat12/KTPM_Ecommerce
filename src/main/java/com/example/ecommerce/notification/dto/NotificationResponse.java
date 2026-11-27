package com.example.ecommerce.notification.dto;

import com.example.ecommerce.notification.model.Notification;
import com.example.ecommerce.notification.model.NotificationChannel;

import java.time.Instant;

public record NotificationResponse(
        String id,
        String userId,
        String recipient,
        NotificationChannel channel,
        String title,
        String content,
        boolean read,
        Instant createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getRecipient(),
                notification.getChannel(),
                notification.getTitle(),
                notification.getContent(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
