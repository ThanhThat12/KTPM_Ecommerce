package com.example.ecommerce.payment.service.adapter;

import com.example.ecommerce.payment.dto.GatewayPaymentResult;
import com.example.ecommerce.payment.model.PaymentMethod;
import com.example.ecommerce.payment.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class VnPayPaymentAdapter implements PaymentGatewayAdapter {

    @Override
    public PaymentMethod supportedMethod() {
        return PaymentMethod.VNPAY;
    }

    @Override
    public GatewayPaymentResult charge(String orderId, BigDecimal amount) {
        String ref = "VNP-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        return new GatewayPaymentResult(PaymentStatus.COMPLETED, ref, "VNPay payment approved");
    }

    @Override
    public GatewayPaymentResult refund(String transactionRef, BigDecimal amount) {
        return new GatewayPaymentResult(PaymentStatus.REFUNDED, transactionRef, "VNPay refund processed");
    }
}
