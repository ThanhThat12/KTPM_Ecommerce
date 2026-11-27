package com.example.ecommerce.notification.service.factory;

import com.example.ecommerce.notification.model.Notification;
import com.example.ecommerce.notification.model.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InAppNotificationSender implements NotificationSender {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.IN_APP;
    }

    @Override
    public void send(Notification notification) {
        log.info("[IN-APP] UserId: {} | Title: {} | Content: {}",
                notification.getUserId(), notification.getTitle(), notification.getContent());
    }
}
