package com.example.ecommerce.payment.service.adapter;

import com.example.ecommerce.payment.dto.GatewayPaymentResult;
import com.example.ecommerce.payment.model.PaymentMethod;
import com.example.ecommerce.payment.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class MoMoPaymentAdapter implements PaymentGatewayAdapter {

    @Override
    public PaymentMethod supportedMethod() {
        return PaymentMethod.MOMO;
    }

    @Override
    public GatewayPaymentResult charge(String orderId, BigDecimal amount) {
        String ref = "MOMO-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        return new GatewayPaymentResult(PaymentStatus.COMPLETED, ref, "MoMo wallet payment approved");
    }

    @Override
    public GatewayPaymentResult refund(String transactionRef, BigDecimal amount) {
        return new GatewayPaymentResult(PaymentStatus.REFUNDED, transactionRef, "MoMo refund processed");
    }
}
