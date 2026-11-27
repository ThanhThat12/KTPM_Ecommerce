package com.example.ecommerce.user.dto;

public record RegisterRequest(
        String email,
        String password,
        String fullName,
        String phone
) {
}
