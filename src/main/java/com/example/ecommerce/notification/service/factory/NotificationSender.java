package com.example.ecommerce.notification.service.factory;

import com.example.ecommerce.notification.model.Notification;
import com.example.ecommerce.notification.model.NotificationChannel;

public interface NotificationSender {

    NotificationChannel channel();

    void send(Notification notification);
}
