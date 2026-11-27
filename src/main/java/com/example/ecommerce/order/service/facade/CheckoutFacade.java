package com.example.ecommerce.order.service.facade;

import com.example.ecommerce.cart.dto.CartSummaryResponse;
import com.example.ecommerce.cart.model.Cart;
import com.example.ecommerce.cart.service.CartService;
import com.example.ecommerce.common.event.OrderPlacedEvent;
import com.example.ecommerce.order.dto.CheckoutRequest;
import com.example.ecommerce.order.dto.OrderResponse;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.OrderItem;
import com.example.ecommerce.order.model.OrderStatus;
import com.example.ecommerce.order.repository.OrderRepository;
import com.example.ecommerce.order.service.state.OrderStateResolver;
import com.example.ecommerce.order.service.validation.CartNotEmptyValidator;
import com.example.ecommerce.order.service.validation.CheckoutContext;
import com.example.ecommerce.order.service.validation.CheckoutValidationHandler;
import com.example.ecommerce.order.service.validation.ShippingAddressValidator;
import com.example.ecommerce.order.service.validation.StockAvailabilityValidator;
import com.example.ecommerce.payment.dto.PaymentRequest;
import com.example.ecommerce.payment.dto.PaymentResponse;
import com.example.ecommerce.payment.model.PaymentStatus;
import com.example.ecommerce.payment.service.PaymentService;
import com.example.ecommerce.product.service.ProductService;
import com.example.ecommerce.user.model.User;
import com.example.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Facade Pattern: provides a single unified entry point for the entire checkout workflow:
 * validation chain -> pricing -> order persistence -> payment -> state transition -> cart clearing -> event publishing.
 */
@Service
@RequiredArgsConstructor
public class CheckoutFacade {

    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final OrderStateResolver stateResolver;
    private final ApplicationEventPublisher eventPublisher;

    public OrderResponse checkout(CheckoutRequest request) {
        User user = userService.getEntityById(request.userId());
        Cart cart = cartService.getOrCreateCart(user.getId());

        // 1. Run Chain of Responsibility validators
        CheckoutValidationHandler validationChain = new CartNotEmptyValidator();
        validationChain
                .linkWith(new ShippingAddressValidator())
                .linkWith(new StockAvailabilityValidator(productService));
        validationChain.validate(new CheckoutContext(user, cart, request));

        // 2. Calculate final price using Strategy + Decorator in CartService
        CartSummaryResponse summary = cartService.buildSummary(cart, request.voucherCode());

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(ci -> OrderItem.builder()
                        .productId(ci.getProductId())
                        .productName(ci.getProductName())
                        .unitPrice(ci.getUnitPrice())
                        .quantity(ci.getQuantity())
                        .build())
                .toList();

        // 3. Persist initial PENDING order
        Order order = Order.builder()
                .userId(user.getId())
                .userEmail(user.getEmail())
                .items(orderItems)
                .subtotal(summary.subtotal())
                .totalAmount(summary.finalTotal())
                .appliedVoucherCode(summary.appliedVoucherCode())
                .priceBreakdown(summary.priceBreakdown())
                .paymentMethod(request.paymentMethod())
                .shippingAddress(request.shippingAddress())
                .status(OrderStatus.PENDING)
                .build();
        order = orderRepository.save(order);

        // 4. Process payment via Factory + Adapter
        PaymentResponse payment = paymentService.processPayment(
                new PaymentRequest(order.getId(), user.getId(), order.getTotalAmount(), request.paymentMethod())
        );
        order.setPaymentTransactionRef(payment.transactionRef());

        if (payment.status() == PaymentStatus.COMPLETED) {
            stateResolver.resolve(order.getStatus()).pay(order);
        }
        order = orderRepository.save(order);

        // 5. Clear cart and notify observers (inventory deduction + email/in-app notification)
        cartService.clearCart(user.getId());
        List<OrderPlacedEvent.OrderItemSnapshot> snapshots = orderItems.stream()
                .map(i -> new OrderPlacedEvent.OrderItemSnapshot(i.getProductId(), i.getQuantity()))
                .toList();

        eventPublisher.publishEvent(new OrderPlacedEvent(
                order.getId(),
                user.getId(),
                user.getEmail(),
                order.getTotalAmount(),
                snapshots,
                Instant.now()
        ));

        return OrderResponse.from(order);
    }
}
