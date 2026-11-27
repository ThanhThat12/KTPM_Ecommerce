package com.example.ecommerce.audit.service;

import com.example.ecommerce.audit.model.AuditLog;
import com.example.ecommerce.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void record(String userId, String email, String role, String method, String uri, int status) {
        AuditLog log = AuditLog.builder()
                .actorUserId(userId)
                .actorEmail(email)
                .actorRole(role)
                .httpMethod(method)
                .requestUri(uri)
                .responseStatus(status)
                .build();
        auditLogRepository.save(log);
    }

    public List<AuditLog> getRecentLogs() {
        return auditLogRepository.findTop100ByOrderByCreatedAtDesc();
    }
}
