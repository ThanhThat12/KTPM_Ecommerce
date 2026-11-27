package com.example.ecommerce.notification.service;

import com.example.ecommerce.common.event.OrderCancelledEvent;
import com.example.ecommerce.common.event.OrderPlacedEvent;
import com.example.ecommerce.notification.dto.NotificationRequest;
import com.example.ecommerce.notification.model.NotificationChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Observer Pattern subscriber: sends order confirmation and cancellation notifications.
 */
@Component
@RequiredArgsConstructor
public class OrderNotificationListener {

    private final NotificationService notificationService;

    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        String title = "Order Confirmed #" + event.orderId();
        String content = "Your order totaling " + event.totalAmount() + " has been placed successfully.";

        notificationService.send(new NotificationRequest(
                event.userId(), event.userEmail(), NotificationChannel.EMAIL, title, content
        ));
        notificationService.send(new NotificationRequest(
                event.userId(), event.userId(), NotificationChannel.IN_APP, title, content
        ));
    }

    @EventListener
    public void onOrderCancelled(OrderCancelledEvent event) {
        String title = "Order Cancelled #" + event.orderId();
        String content = "Your order has been cancelled. Reason: " + event.reason();

        notificationService.send(new NotificationRequest(
                event.userId(), event.userEmail(), NotificationChannel.EMAIL, title, content
        ));
        notificationService.send(new NotificationRequest(
                event.userId(), event.userId(), NotificationChannel.IN_APP, title, content
        ));
    }
}
