package com.example.ecommerce.payment.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.payment.dto.PaymentRequest;
import com.example.ecommerce.payment.dto.PaymentResponse;
import com.example.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Payment processed", paymentService.processPayment(request)));
    }

    @PostMapping("/orders/{orderId}/refund")
    public ResponseEntity<ApiResponse<PaymentResponse>> refund(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponse.ok("Payment refunded", paymentService.refundByOrderId(orderId)));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.getByOrderId(orderId)));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.getByUserId(userId)));
    }
}
