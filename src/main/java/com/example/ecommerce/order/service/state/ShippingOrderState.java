package com.example.ecommerce.order.service.state;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class ShippingOrderState implements OrderState {

    @Override
    public OrderStatus status() {
        return OrderStatus.SHIPPING;
    }

    @Override
    public void deliver(Order order) {
        order.setStatus(OrderStatus.DELIVERED);
    }
}
