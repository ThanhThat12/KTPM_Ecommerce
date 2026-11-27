package com.example.ecommerce.wishlist.dto;

import com.example.ecommerce.product.dto.ProductResponse;

import java.util.List;

public record WishlistResponse(
        String userId,
        List<ProductResponse> products
) {
}
