package com.example.ecommerce.analytics.dto;

import java.time.Instant;
import java.util.Map;

public record ReportResponse(
        String reportName,
        Instant generatedAt,
        Map<String, Object> metrics
) {
}
