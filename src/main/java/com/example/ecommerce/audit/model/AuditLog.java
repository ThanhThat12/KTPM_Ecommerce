package com.example.ecommerce.audit.model;

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
@Document(collection = "audit_logs")
public class AuditLog extends BaseDocument {

    private String actorUserId;

    private String actorEmail;

    private String actorRole;

    private String httpMethod;

    private String requestUri;

    private int responseStatus;
}
