package com.example.ecommerce.payment.service;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.payment.dto.GatewayPaymentResult;
import com.example.ecommerce.payment.dto.PaymentRequest;
import com.example.ecommerce.payment.dto.PaymentResponse;
import com.example.ecommerce.payment.model.Payment;
import com.example.ecommerce.payment.repository.PaymentRepository;
import com.example.ecommerce.payment.service.adapter.PaymentGatewayAdapter;
import com.example.ecommerce.payment.service.factory.PaymentGatewayFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayFactory paymentGatewayFactory;

    public PaymentResponse processPayment(PaymentRequest request) {
        if (request == null || request.orderId() == null || request.amount() == null || request.method() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Order ID, amount, and payment method are required");
        }

        PaymentGatewayAdapter adapter = paymentGatewayFactory.getAdapter(request.method());
        GatewayPaymentResult result = adapter.charge(request.orderId(), request.amount());

        Payment payment = Payment.builder()
                .orderId(request.orderId())
                .userId(request.userId())
                .amount(request.amount())
                .method(request.method())
                .status(result.status())
                .transactionRef(result.transactionRef())
                .gatewayMessage(result.message())
                .build();

        return PaymentResponse.from(paymentRepository.save(payment));
    }

    public PaymentResponse refundByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Payment not found"));
        PaymentGatewayAdapter adapter = paymentGatewayFactory.getAdapter(payment.getMethod());
        GatewayPaymentResult result = adapter.refund(payment.getTransactionRef(), payment.getAmount());
        payment.setStatus(result.status());
        payment.setGatewayMessage(result.message());
        return PaymentResponse.from(paymentRepository.save(payment));
    }

    public PaymentResponse getByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(PaymentResponse::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Payment not found"));
    }

    public List<PaymentResponse> getByUserId(String userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(PaymentResponse::from)
                .toList();
    }
}
