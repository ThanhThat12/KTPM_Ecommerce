package com.example.ecommerce.notification.service;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.notification.dto.NotificationRequest;
import com.example.ecommerce.notification.dto.NotificationResponse;
import com.example.ecommerce.notification.model.Notification;
import com.example.ecommerce.notification.repository.NotificationRepository;
import com.example.ecommerce.notification.service.factory.NotificationSender;
import com.example.ecommerce.notification.service.factory.NotificationSenderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSenderFactory senderFactory;

    public NotificationResponse send(NotificationRequest request) {
        if (request == null || request.userId() == null || request.channel() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "User ID and notification channel are required");
        }

        Notification notification = Notification.builder()
                .userId(request.userId())
                .recipient(request.recipient())
                .channel(request.channel())
                .title(request.title())
                .content(request.content())
                .read(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        NotificationSender sender = senderFactory.getSender(request.channel());
        sender.send(saved);
        return NotificationResponse.from(saved);
    }

    public List<NotificationResponse> getByUserId(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    public NotificationResponse markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Notification not found"));
        notification.setRead(true);
        return NotificationResponse.from(notificationRepository.save(notification));
    }
}
