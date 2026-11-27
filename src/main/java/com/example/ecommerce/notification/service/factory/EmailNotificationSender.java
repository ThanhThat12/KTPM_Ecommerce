package com.example.ecommerce.notification.service.factory;

import com.example.ecommerce.notification.model.Notification;
import com.example.ecommerce.notification.model.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationSender implements NotificationSender {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public void send(Notification notification) {
        log.info("[EMAIL] To: {} | Subject: {} | Body: {}",
                notification.getRecipient(), notification.getTitle(), notification.getContent());
    }
}
