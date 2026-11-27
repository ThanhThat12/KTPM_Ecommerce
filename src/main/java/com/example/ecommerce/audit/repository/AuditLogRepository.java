package com.example.ecommerce.audit.repository;

import com.example.ecommerce.audit.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

    List<AuditLog> findTop100ByOrderByCreatedAtDesc();
}
