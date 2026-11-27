package com.example.ecommerce.user.dto;

public record UpdateProfileRequest(
        String fullName,
        String phone
) {
}
