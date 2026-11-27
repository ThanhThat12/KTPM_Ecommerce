package com.example.ecommerce.order.service.state;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class PendingOrderState implements OrderState {

    @Override
    public OrderStatus status() {
        return OrderStatus.PENDING;
    }

    @Override
    public void pay(Order order) {
        order.setStatus(OrderStatus.PAID);
    }

    @Override
    public void cancel(Order order, String reason) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(reason);
    }
}
