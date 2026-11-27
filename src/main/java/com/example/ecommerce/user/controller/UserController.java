package com.example.ecommerce.user.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.common.security.AuthenticatedUser;
import com.example.ecommerce.user.dto.AuthResponse;
import com.example.ecommerce.user.dto.LoginRequest;
import com.example.ecommerce.user.dto.RegisterRequest;
import com.example.ecommerce.user.dto.UpdateProfileRequest;
import com.example.ecommerce.user.dto.UserResponse;
import com.example.ecommerce.user.model.Address;
import com.example.ecommerce.user.model.UserRole;
import com.example.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("User registered successfully", response));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Login successful", userService.login(request)));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getById(currentUser.getUserId())));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getAll()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getById(id)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable String id,
            @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", userService.updateProfile(id, request)));
    }

    @PostMapping("/users/{id}/addresses")
    public ResponseEntity<ApiResponse<UserResponse>> addAddress(
            @PathVariable String id,
            @RequestBody Address address
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Address added", userService.addAddress(id, address)));
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserResponse>> updateRole(
            @PathVariable String id,
            @RequestParam UserRole role
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Role updated", userService.updateRole(id, role)));
    }
}
