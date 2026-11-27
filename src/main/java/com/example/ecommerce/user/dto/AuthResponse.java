package com.example.ecommerce.user.dto;

public record AuthResponse(
        String accessToken,
        UserResponse user
) {
}
