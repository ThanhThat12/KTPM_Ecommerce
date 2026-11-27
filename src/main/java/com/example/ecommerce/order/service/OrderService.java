package com.example.ecommerce.order.service;

import com.example.ecommerce.common.event.OrderCancelledEvent;
import com.example.ecommerce.common.event.OrderPlacedEvent;
import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.order.dto.OrderResponse;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.order.service.state.OrderStateResolver;
import com.example.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStateResolver stateResolver;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;

    public Order getEntityById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }

    public OrderResponse getById(String orderId) {
        return OrderResponse.from(getEntityById(orderId));
    }

    public List<OrderResponse> getByUserId(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse markAsPaid(String orderId) {
        Order order = getEntityById(orderId);
        stateResolver.resolve(order.getStatus()).pay(order);
        return OrderResponse.from(orderRepository.save(order));
    }

    public OrderResponse shipOrder(String orderId) {
        Order order = getEntityById(orderId);
        stateResolver.resolve(order.getStatus()).ship(order);
        return OrderResponse.from(orderRepository.save(order));
    }

    public OrderResponse deliverOrder(String orderId) {
        Order order = getEntityById(orderId);
        stateResolver.resolve(order.getStatus()).deliver(order);
        return OrderResponse.from(orderRepository.save(order));
    }

    public OrderResponse cancelOrder(String orderId, String reason) {
        Order order = getEntityById(orderId);
        String effectiveReason = (reason == null || reason.isBlank()) ? "Cancelled by user" : reason.trim();

        stateResolver.resolve(order.getStatus()).cancel(order, effectiveReason);
        Order saved = orderRepository.save(order);

        if (saved.getPaymentTransactionRef() != null) {
            paymentService.refundByOrderId(saved.getId());
        }

        List<OrderPlacedEvent.OrderItemSnapshot> snapshots = saved.getItems().stream()
                .map(i -> new OrderPlacedEvent.OrderItemSnapshot(i.getProductId(), i.getQuantity()))
                .toList();

        eventPublisher.publishEvent(new OrderCancelledEvent(
                saved.getId(),
                saved.getUserId(),
                saved.getUserEmail(),
                effectiveReason,
                snapshots,
                Instant.now()
        ));

        return OrderResponse.from(saved);
    }
}
