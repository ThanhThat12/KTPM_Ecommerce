package com.example.ecommerce.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR("COMMON_500", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    INVALID_REQUEST("COMMON_400", HttpStatus.BAD_REQUEST, "Invalid request"),
    RESOURCE_NOT_FOUND("COMMON_404", HttpStatus.NOT_FOUND, "Resource not found"),
    UNAUTHORIZED("COMMON_401", HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN("COMMON_403", HttpStatus.FORBIDDEN, "Access denied"),

    // User & Auth
    USER_NOT_FOUND("USER_404", HttpStatus.NOT_FOUND, "User not found"),
    EMAIL_ALREADY_EXISTS("USER_409", HttpStatus.CONFLICT, "Email already exists"),

    // Product & Inventory
    PRODUCT_NOT_FOUND("PRODUCT_404", HttpStatus.NOT_FOUND, "Product not found"),
    OUT_OF_STOCK("PRODUCT_409", HttpStatus.CONFLICT, "Product is out of stock"),

    // Cart & Promotion
    CART_EMPTY("CART_400", HttpStatus.BAD_REQUEST, "Cart is empty"),
    INVALID_VOUCHER("PROMOTION_400", HttpStatus.BAD_REQUEST, "Voucher is invalid or expired"),

    // Order
    ORDER_NOT_FOUND("ORDER_404", HttpStatus.NOT_FOUND, "Order not found"),
    INVALID_ORDER_STATE("ORDER_409", HttpStatus.CONFLICT, "Invalid order state transition"),

    // Payment
    PAYMENT_FAILED("PAYMENT_400", HttpStatus.BAD_REQUEST, "Payment processing failed"),
    UNSUPPORTED_PAYMENT_METHOD("PAYMENT_400", HttpStatus.BAD_REQUEST, "Unsupported payment method");

    private final String code;
    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(String code, HttpStatus status, String defaultMessage) {
        this.code = code;
        this.status = status;
        this.defaultMessage = defaultMessage;
    }
}
