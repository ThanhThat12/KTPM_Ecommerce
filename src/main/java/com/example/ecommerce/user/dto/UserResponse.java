package com.example.ecommerce.user.dto;

import com.example.ecommerce.user.model.Address;
import com.example.ecommerce.user.model.User;
import com.example.ecommerce.user.model.UserRole;

import java.time.Instant;
import java.util.List;

public record UserResponse(
        String id,
        String email,
        String fullName,
        String phone,
        UserRole role,
        boolean active,
        List<Address> addresses,
        Instant createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getRole(),
                user.isActive(),
                user.getAddresses(),
                user.getCreatedAt()
        );
    }
}
