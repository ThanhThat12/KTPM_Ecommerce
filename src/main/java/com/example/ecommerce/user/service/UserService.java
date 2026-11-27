package com.example.ecommerce.user.service;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.common.security.JwtService;
import com.example.ecommerce.common.security.RefreshToken;
import com.example.ecommerce.common.security.RefreshTokenService;
import com.example.ecommerce.common.security.TokenBlacklistService;
import com.example.ecommerce.user.dto.AuthResponse;
import com.example.ecommerce.user.dto.LoginRequest;
import com.example.ecommerce.user.dto.RegisterRequest;
import com.example.ecommerce.user.dto.UpdateProfileRequest;
import com.example.ecommerce.user.dto.UserResponse;
import com.example.ecommerce.user.model.Address;
import com.example.ecommerce.user.model.User;
import com.example.ecommerce.user.model.UserRole;
import com.example.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthResponse register(RegisterRequest request) {
        validateRegistration(request);
        String normalizedEmail = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName().trim())
                .phone(request.phone())
                .role(UserRole.CUSTOMER)
                .active(true)
                .build();

        User saved = userRepository.save(user);
        RefreshToken refresh = refreshTokenService.createForUser(saved.getId());
        return new AuthResponse(jwtService.generateToken(saved), refresh.getToken(), UserResponse.from(saved));
    }

    public AuthResponse login(LoginRequest request) {
        if (request == null || isBlank(request.email()) || isBlank(request.password())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Email and password are required");
        }

        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid email or password"));

        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid email or password");
        }

        RefreshToken refresh = refreshTokenService.createForUser(user.getId());
        return new AuthResponse(jwtService.generateToken(user), refresh.getToken(), UserResponse.from(user));
    }

    public AuthResponse refreshAccessToken(String refreshTokenValue) {
        if (isBlank(refreshTokenValue)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Refresh token is required");
        }
        RefreshToken existing = refreshTokenService.verify(refreshTokenValue);
        User user = getEntityById(existing.getUserId());
        refreshTokenService.revoke(refreshTokenValue);
        RefreshToken next = refreshTokenService.createForUser(user.getId());
        return new AuthResponse(jwtService.generateToken(user), next.getToken(), UserResponse.from(user));
    }

    public void logout(String userId, String accessToken) {
        tokenBlacklistService.blacklist(accessToken, Instant.now().plusSeconds(86400));
        refreshTokenService.revokeAllForUser(userId);
    }

    public User getEntityById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponse getById(String userId) {
        return UserResponse.from(getEntityById(userId));
    }

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse updateProfile(String userId, UpdateProfileRequest request) {
        User user = getEntityById(userId);
        if (request.fullName() != null && !request.fullName().isBlank()) {
            user.setFullName(request.fullName().trim());
        }
        if (request.phone() != null && !request.phone().isBlank()) {
            user.setPhone(request.phone().trim());
        }
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse addAddress(String userId, Address address) {
        if (address == null || isBlank(address.getStreet()) || isBlank(address.getCity())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Street and city are required");
        }
        User user = getEntityById(userId);
        if (address.isDefaultAddress()) {
            user.getAddresses().forEach(existing -> existing.setDefaultAddress(false));
        }
        user.getAddresses().add(address);
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse updateRole(String userId, UserRole role) {
        if (role == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Role is required");
        }
        User user = getEntityById(userId);
        user.setRole(role);
        return UserResponse.from(userRepository.save(user));
    }

    private void validateRegistration(RegisterRequest request) {
        if (request == null || isBlank(request.email()) || !request.email().contains("@")) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Valid email is required");
        }
        if (isBlank(request.password()) || request.password().length() < 6) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Password must be at least 6 characters");
        }
        if (isBlank(request.fullName())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Full name is required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
