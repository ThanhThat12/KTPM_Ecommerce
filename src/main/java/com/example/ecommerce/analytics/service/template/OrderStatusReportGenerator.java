package com.example.ecommerce.analytics.service.template;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.OrderStatus;
import com.example.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderStatusReportGenerator extends AbstractReportGenerator<Order> {

    private final OrderRepository orderRepository;

    @Override
    protected String reportName() {
        return "ORDER_STATUS_BREAKDOWN_REPORT";
    }

    @Override
    protected List<Order> fetchRawData() {
        return orderRepository.findAll();
    }

    @Override
    protected Map<String, Object> aggregateMetrics(List<Order> orders) {
        Map<String, Object> counts = new LinkedHashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            long count = orders.stream().filter(o -> o.getStatus() == status).count();
            counts.put(status.name(), count);
        }
        counts.put("TOTAL", orders.size());
        return counts;
    }
}
