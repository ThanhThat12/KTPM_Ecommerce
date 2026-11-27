package com.example.ecommerce.product.service;

import com.example.ecommerce.common.event.OrderCancelledEvent;
import com.example.ecommerce.common.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Observer Pattern subscriber: automatically adjusts product stock
 * when orders are placed or cancelled.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductInventoryListener {

    private final ProductService productService;

    @EventListener
    public void onOrderPlaced(OrderPlacedEvent event) {
        log.info("Deducting stock for order {}", event.orderId());
        for (OrderPlacedEvent.OrderItemSnapshot item : event.items()) {
            productService.deductStock(item.productId(), item.quantity());
        }
    }

    @EventListener
    public void onOrderCancelled(OrderCancelledEvent event) {
        log.info("Restoring stock for cancelled order {}", event.orderId());
        for (OrderPlacedEvent.OrderItemSnapshot item : event.items()) {
            productService.restoreStock(item.productId(), item.quantity());
        }
    }
}
