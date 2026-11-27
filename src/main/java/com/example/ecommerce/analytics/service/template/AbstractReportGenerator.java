package com.example.ecommerce.analytics.service.template;

import com.example.ecommerce.analytics.dto.ReportResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Template Method Pattern: defines the skeleton of report generation
 * while letting subclasses provide data fetching and metric aggregation.
 */
public abstract class AbstractReportGenerator<T> {

    public final ReportResponse generate() {
        List<T> rawData = fetchRawData();
        Map<String, Object> metrics = aggregateMetrics(rawData);
        return new ReportResponse(reportName(), Instant.now(), metrics);
    }

    protected abstract String reportName();

    protected abstract List<T> fetchRawData();

    protected abstract Map<String, Object> aggregateMetrics(List<T> rawData);
}
