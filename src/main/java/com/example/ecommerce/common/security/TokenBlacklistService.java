package com.example.ecommerce.common.security;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks revoked access tokens (logout) until their natural expiration time.
 */
@Service
public class TokenBlacklistService {

    private final Map<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklist(String token, Instant expiresAt) {
        if (token != null && !token.isBlank()) {
            blacklistedTokens.put(token, expiresAt != null ? expiresAt : Instant.now().plusSeconds(86400));
        }
    }

    public boolean isBlacklisted(String token) {
        Instant exp = blacklistedTokens.get(token);
        if (exp == null) {
            return false;
        }
        if (Instant.now().isAfter(exp)) {
            blacklistedTokens.remove(token);
            return false;
        }
        return true;
    }

    @Scheduled(fixedRate = 300000)
    public void cleanupExpired() {
        Instant now = Instant.now();
        blacklistedTokens.entrySet().removeIf(entry -> now.isAfter(entry.getValue()));
    }
}
