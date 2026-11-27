package com.example.ecommerce.payment.service.adapter;

import com.example.ecommerce.payment.dto.GatewayPaymentResult;
import com.example.ecommerce.payment.model.PaymentMethod;

import java.math.BigDecimal;

/**
 * Adapter Pattern target interface normalizing different payment providers
 * (COD, VNPay, MoMo) behind a single contract.
 */
public interface PaymentGatewayAdapter {

    PaymentMethod supportedMethod();

    GatewayPaymentResult charge(String orderId, BigDecimal amount);

    GatewayPaymentResult refund(String transactionRef, BigDecimal amount);
}
