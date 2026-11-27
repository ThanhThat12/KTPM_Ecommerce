package com.example.ecommerce.user.dto;

public record LoginRequest(
        String email,
        String password
) {
}
