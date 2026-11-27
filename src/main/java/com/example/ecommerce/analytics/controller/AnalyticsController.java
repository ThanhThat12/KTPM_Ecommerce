package com.example.ecommerce.analytics.controller;

import com.example.ecommerce.analytics.dto.ReportResponse;
import com.example.ecommerce.analytics.dto.TopProductResponse;
import com.example.ecommerce.analytics.service.AnalyticsService;
import com.example.ecommerce.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<ReportResponse>> getRevenueReport() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.getRevenueReport()));
    }

    @GetMapping("/orders-by-status")
    public ResponseEntity<ApiResponse<ReportResponse>> getOrderStatusReport() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.getOrderStatusReport()));
    }

    @GetMapping("/top-products")
    public ResponseEntity<ApiResponse<List<TopProductResponse>>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.getTopProducts(limit)));
    }
}
