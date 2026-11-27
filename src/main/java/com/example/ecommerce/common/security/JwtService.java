package com.example.ecommerce.common.security;

import com.example.ecommerce.user.model.User;
import com.example.ecommerce.user.model.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RFC 7519 HS256 JWT token generator and validator.
 */
@Service
public class JwtService {

    private static final String HEADER_JSON = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String HMAC_SHA256 = "HmacSHA256";

    private static final Pattern STRING_CLAIM = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern NUMBER_CLAIM = Pattern.compile("\"([^\"]+)\"\\s*:\\s*(\\d+)");

    private final byte[] secretKeyBytes;
    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms:86400000}") long expirationMs
    ) {
        this.secretKeyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMs = expirationMs;
    }

    public String generateToken(User user) {
        long nowSeconds = Instant.now().getEpochSecond();
        long expSeconds = nowSeconds + (expirationMs / 1000);

        String payloadJson = "{"
                + "\"sub\":\"" + escapeJson(user.getId()) + "\","
                + "\"email\":\"" + escapeJson(user.getEmail()) + "\","
                + "\"role\":\"" + user.getRole().name() + "\","
                + "\"iat\":" + nowSeconds + ","
                + "\"exp\":" + expSeconds
                + "}";

        String encodedHeader = base64UrlEncode(HEADER_JSON.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signingInput = encodedHeader + "." + encodedPayload;
        String signature = base64UrlEncode(sign(signingInput));

        return signingInput + "." + signature;
    }

    public Optional<AuthenticatedUser> parseAndValidate(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return Optional.empty();
        }

        try {
            String signingInput = parts[0] + "." + parts[1];
            byte[] expectedSignature = sign(signingInput);
            byte[] actualSignature = Base64.getUrlDecoder().decode(parts[2]);
            if (!MessageDigest.isEqual(expectedSignature, actualSignature)) {
                return Optional.empty();
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            String userId = extractStringClaim(payloadJson, "sub");
            String email = extractStringClaim(payloadJson, "email");
            String roleStr = extractStringClaim(payloadJson, "role");
            Long exp = extractNumberClaim(payloadJson, "exp");

            if (userId == null || email == null || roleStr == null || exp == null) {
                return Optional.empty();
            }
            if (Instant.now().getEpochSecond() >= exp) {
                return Optional.empty();
            }

            UserRole role = UserRole.valueOf(roleStr);
            return Optional.of(new AuthenticatedUser(userId, email, role));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private byte[] sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secretKeyBytes, HMAC_SHA256));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to sign JWT token", e);
        }
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String escapeJson(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String extractStringClaim(String json, String key) {
        Matcher matcher = STRING_CLAIM.matcher(json);
        while (matcher.find()) {
            if (key.equals(matcher.group(1))) {
                return matcher.group(2);
            }
        }
        return null;
    }

    private Long extractNumberClaim(String json, String key) {
        Matcher matcher = NUMBER_CLAIM.matcher(json);
        while (matcher.find()) {
            if (key.equals(matcher.group(1))) {
                return Long.parseLong(matcher.group(2));
            }
        }
        return null;
    }
}
