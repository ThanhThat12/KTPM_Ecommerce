package com.example.ecommerce.order.dto;

import com.example.ecommerce.payment.model.PaymentMethod;
import com.example.ecommerce.user.model.Address;

public record CheckoutRequest(
        String userId,
        PaymentMethod paymentMethod,
        String voucherCode,
        Address shippingAddress
) {
}
