package com.example.ecommerce.notification.service.factory;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.notification.model.NotificationChannel;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Factory Method Pattern: selects the concrete NotificationSender by channel.
 */
@Component
public class NotificationSenderFactory {

    private final Map<NotificationChannel, NotificationSender> senders = new EnumMap<>(NotificationChannel.class);

    public NotificationSenderFactory(List<NotificationSender> senderList) {
        for (NotificationSender sender : senderList) {
            senders.put(sender.channel(), sender);
        }
    }

    public NotificationSender getSender(NotificationChannel channel) {
        NotificationSender sender = senders.get(channel);
        if (sender == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Unsupported notification channel: " + channel);
        }
        return sender;
    }
}
