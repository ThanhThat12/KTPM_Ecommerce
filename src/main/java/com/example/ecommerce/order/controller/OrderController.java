package com.example.ecommerce.order.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.order.dto.CancelOrderRequest;
import com.example.ecommerce.order.dto.CheckoutRequest;
import com.example.ecommerce.order.dto.OrderResponse;
import com.example.ecommerce.order.service.OrderService;
import com.example.ecommerce.order.service.facade.CheckoutFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CheckoutFacade checkoutFacade;
    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderResponse>> checkout(@RequestBody CheckoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Order placed successfully", checkoutFacade.checkout(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getById(id)));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUser(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getByUserId(userId)));
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<OrderResponse>> markPaid(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok("Order marked as paid", orderService.markAsPaid(id)));
    }

    @PatchMapping("/{id}/ship")
    public ResponseEntity<ApiResponse<OrderResponse>> ship(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok("Order is now shipping", orderService.shipOrder(id)));
    }

    @PatchMapping("/{id}/deliver")
    public ResponseEntity<ApiResponse<OrderResponse>> deliver(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok("Order delivered", orderService.deliverOrder(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancel(
            @PathVariable String id,
            @RequestBody(required = false) CancelOrderRequest request
    ) {
        String reason = request != null ? request.reason() : null;
        return ResponseEntity.ok(ApiResponse.ok("Order cancelled", orderService.cancelOrder(id, reason)));
    }
}
