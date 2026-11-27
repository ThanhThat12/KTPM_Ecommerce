package com.example.ecommerce.common;

import com.example.ecommerce.common.security.AuthenticatedUser;
import com.example.ecommerce.common.security.JwtService;
import com.example.ecommerce.user.model.User;
import com.example.ecommerce.user.model.UserRole;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService("test-secret-key-for-unit-tests-2026", 60000);

    @Test
    void shouldGenerateAndValidateHs256JwtToken() {
        User user = User.builder()
                .email("alice@example.com")
                .role(UserRole.ADMIN)
                .build();
        user.setId("user-123");

        String token = jwtService.generateToken(user);
        Optional<AuthenticatedUser> parsed = jwtService.parseAndValidate(token);

        assertTrue(parsed.isPresent());
        assertEquals("user-123", parsed.get().getUserId());
        assertEquals("alice@example.com", parsed.get().getEmail());
        assertEquals(UserRole.ADMIN, parsed.get().getRole());
    }

    @Test
    void shouldRejectTamperedToken() {
        User user = User.builder().email("bob@example.com").role(UserRole.CUSTOMER).build();
        user.setId("user-456");

        String token = jwtService.generateToken(user);
        String tampered = token.substring(0, token.length() - 3) + "xyz";

        assertTrue(jwtService.parseAndValidate(tampered).isEmpty());
    }
}
