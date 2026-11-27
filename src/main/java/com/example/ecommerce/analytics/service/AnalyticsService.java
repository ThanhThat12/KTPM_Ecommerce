package com.example.ecommerce.analytics.service;

import com.example.ecommerce.analytics.dto.ReportResponse;
import com.example.ecommerce.analytics.dto.TopProductResponse;
import com.example.ecommerce.analytics.service.template.OrderStatusReportGenerator;
import com.example.ecommerce.analytics.service.template.RevenueReportGenerator;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.OrderItem;
import com.example.ecommerce.order.model.OrderStatus;
import com.example.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final RevenueReportGenerator revenueReportGenerator;
    private final OrderStatusReportGenerator orderStatusReportGenerator;
    private final OrderRepository orderRepository;

    public ReportResponse getRevenueReport() {
        return revenueReportGenerator.generate();
    }

    public ReportResponse getOrderStatusReport() {
        return orderStatusReportGenerator.generate();
    }

    public List<TopProductResponse> getTopProducts(int limit) {
        int safeLimit = Math.min(Math.max(1, limit), 50);
        Map<String, TopProductAccumulator> map = new HashMap<>();

        for (Order order : orderRepository.findAll()) {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                continue;
            }
            for (OrderItem item : order.getItems()) {
                TopProductAccumulator acc = map.computeIfAbsent(
                        item.getProductId(),
                        id -> new TopProductAccumulator(id, item.getProductName())
                );
                acc.units += item.getQuantity();
                acc.revenue = acc.revenue.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        return map.values().stream()
                .sorted(Comparator.comparingLong((TopProductAccumulator a) -> a.units).reversed())
                .limit(safeLimit)
                .map(a -> new TopProductResponse(a.productId, a.productName, a.units, a.revenue))
                .toList();
    }

    private static class TopProductAccumulator {
        final String productId;
        final String productName;
        long units = 0;
        BigDecimal revenue = BigDecimal.ZERO;

        TopProductAccumulator(String productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }
    }
}
