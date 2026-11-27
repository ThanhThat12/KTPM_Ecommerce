package com.example.ecommerce.common.security;

import com.example.ecommerce.user.model.User;
import com.example.ecommerce.user.model.UserRole;
import com.example.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a default ADMIN account on startup if it does not already exist in MongoDB.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@ecommerce.com}")
    private String adminEmail;

    @Value("${app.admin.password:Admin@123}")
    private String adminPassword;

    @Value("${app.admin.full-name:System Administrator}")
    private String adminFullName;

    @Override
    public void run(String... args) {
        String normalizedEmail = adminEmail.trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            return;
        }
        User admin = User.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .fullName(adminFullName)
                .role(UserRole.ADMIN)
                .active(true)
                .build();
        userRepository.save(admin);
        log.info("Default ADMIN account seeded: {}", normalizedEmail);
    }
}
