package com.example.ecommerce.cart.dto;

public record AddCartItemRequest(
        String productId,
        int quantity
) {
}
