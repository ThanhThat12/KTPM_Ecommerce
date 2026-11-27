package com.example.ecommerce.order;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.OrderStatus;
import com.example.ecommerce.order.service.state.CancelledOrderState;
import com.example.ecommerce.order.service.state.DeliveredOrderState;
import com.example.ecommerce.order.service.state.OrderStateResolver;
import com.example.ecommerce.order.service.state.PaidOrderState;
import com.example.ecommerce.order.service.state.PendingOrderState;
import com.example.ecommerce.order.service.state.ShippingOrderState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderStateTest {

    private OrderStateResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new OrderStateResolver(List.of(
                new PendingOrderState(),
                new PaidOrderState(),
                new ShippingOrderState(),
                new DeliveredOrderState(),
                new CancelledOrderState()
        ));
    }

    @Test
    void shouldFollowHappyLifecyclePendingToDelivered() {
        Order order = Order.builder().status(OrderStatus.PENDING).build();

        resolver.resolve(order.getStatus()).pay(order);
        assertEquals(OrderStatus.PAID, order.getStatus());

        resolver.resolve(order.getStatus()).ship(order);
        assertEquals(OrderStatus.SHIPPING, order.getStatus());

        resolver.resolve(order.getStatus()).deliver(order);
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void shouldRejectCancelAfterDelivered() {
        Order order = Order.builder().status(OrderStatus.DELIVERED).build();

        assertThrows(BusinessException.class,
                () -> resolver.resolve(order.getStatus()).cancel(order, "Too late"));
    }

    @Test
    void shouldRejectShipBeforePaid() {
        Order order = Order.builder().status(OrderStatus.PENDING).build();

        assertThrows(BusinessException.class,
                () -> resolver.resolve(order.getStatus()).ship(order));
    }
}
