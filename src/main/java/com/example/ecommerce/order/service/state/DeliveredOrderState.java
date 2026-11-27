package com.example.ecommerce.order.service.state;

import com.example.ecommerce.order.model.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class DeliveredOrderState implements OrderState {

    @Override
    public OrderStatus status() {
        return OrderStatus.DELIVERED;
    }
}
