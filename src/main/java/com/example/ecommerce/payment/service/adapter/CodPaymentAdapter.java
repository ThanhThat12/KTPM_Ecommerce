package com.example.ecommerce.payment.service.adapter;

import com.example.ecommerce.payment.dto.GatewayPaymentResult;
import com.example.ecommerce.payment.model.PaymentMethod;
import com.example.ecommerce.payment.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class CodPaymentAdapter implements PaymentGatewayAdapter {

    @Override
    public PaymentMethod supportedMethod() {
        return PaymentMethod.COD;
    }

    @Override
    public GatewayPaymentResult charge(String orderId, BigDecimal amount) {
        String ref = "COD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new GatewayPaymentResult(PaymentStatus.PENDING, ref, "Cash on delivery scheduled");
    }

    @Override
    public GatewayPaymentResult refund(String transactionRef, BigDecimal amount) {
        return new GatewayPaymentResult(PaymentStatus.REFUNDED, transactionRef, "COD order cancelled prior to collection");
    }
}
