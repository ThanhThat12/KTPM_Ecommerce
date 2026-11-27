package com.example.ecommerce.order.dto;

import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.OrderItem;
import com.example.ecommerce.order.model.OrderStatus;
import com.example.ecommerce.payment.model.PaymentMethod;
import com.example.ecommerce.user.model.Address;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        String id,
        String userId,
        List<OrderItem> items,
        BigDecimal subtotal,
        BigDecimal totalAmount,
        String appliedVoucherCode,
        List<String> priceBreakdown,
        PaymentMethod paymentMethod,
        String paymentTransactionRef,
        Address shippingAddress,
        OrderStatus status,
        String cancelReason,
        Instant createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getItems(),
                order.getSubtotal(),
                order.getTotalAmount(),
                order.getAppliedVoucherCode(),
                order.getPriceBreakdown(),
                order.getPaymentMethod(),
                order.getPaymentTransactionRef(),
                order.getShippingAddress(),
                order.getStatus(),
                order.getCancelReason(),
                order.getCreatedAt()
        );
    }
}
