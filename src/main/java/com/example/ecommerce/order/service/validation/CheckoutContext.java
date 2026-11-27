package com.example.ecommerce.order.service.validation;

import com.example.ecommerce.cart.model.Cart;
import com.example.ecommerce.order.dto.CheckoutRequest;
import com.example.ecommerce.user.model.User;

public record CheckoutContext(
        User user,
        Cart cart,
        CheckoutRequest request
) {
}
