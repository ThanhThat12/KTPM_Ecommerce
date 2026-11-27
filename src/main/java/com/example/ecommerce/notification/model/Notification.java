package com.example.ecommerce.notification.model;

import com.example.ecommerce.common.model.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification extends BaseDocument {

    private String userId;

    private String recipient;

    private NotificationChannel channel;

    private String title;

    private String content;

    @Builder.Default
    private boolean read = false;
}
