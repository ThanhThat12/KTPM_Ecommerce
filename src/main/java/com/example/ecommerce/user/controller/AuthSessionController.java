package com.example.ecommerce.user.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.common.security.AuthenticatedUser;
import com.example.ecommerce.user.dto.AuthResponse;
import com.example.ecommerce.user.dto.RefreshTokenRequest;
import com.example.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthSessionController {

    private final UserService userService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = userService.refreshAccessToken(request != null ? request.refreshToken() : null);
        return ResponseEntity.ok(ApiResponse.ok("Token refreshed", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader
    ) {
        String token = (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7).trim()
                : null;
        userService.logout(currentUser.getUserId(), token);
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully", null));
    }
}
