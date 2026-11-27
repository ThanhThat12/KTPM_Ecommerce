package com.example.ecommerce.product.dto;

public record CategoryRequest(
        String slug,
        String name,
        String description
) {
}
