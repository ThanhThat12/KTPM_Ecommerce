package com.example.ecommerce.analytics.service.template;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.OrderStatus;
import com.example.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RevenueReportGenerator extends AbstractReportGenerator<Order> {

    private final OrderRepository orderRepository;

    @Override
    protected String reportName() {
        return "REVENUE_SUMMARY_REPORT";
    }

    @Override
    protected List<Order> fetchRawData() {
        return orderRepository.findAll();
    }

    @Override
    protected Map<String, Object> aggregateMetrics(List<Order> orders) {
        List<Order> validOrders = orders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .toList();

        BigDecimal totalRevenue = validOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avgOrderValue = validOrders.isEmpty()
                ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(validOrders.size()), 2, RoundingMode.HALF_UP);

        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("totalOrders", orders.size());
        metrics.put("completedOrActiveOrders", validOrders.size());
        metrics.put("totalRevenue", totalRevenue);
        metrics.put("averageOrderValue", avgOrderValue);
        return metrics;
    }
}
