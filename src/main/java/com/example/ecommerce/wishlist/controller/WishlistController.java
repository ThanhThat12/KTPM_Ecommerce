package com.example.ecommerce.wishlist.controller;

import com.example.ecommerce.cart.dto.CartSummaryResponse;
import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.common.security.AuthenticatedUser;
import com.example.ecommerce.wishlist.dto.WishlistResponse;
import com.example.ecommerce.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<WishlistResponse>> getMyWishlist(
            @AuthenticationPrincipal AuthenticatedUser currentUser
    ) {
        return ResponseEntity.ok(ApiResponse.ok(wishlistService.getWishlist(currentUser.getUserId())));
    }

    @PostMapping("/me/products/{productId}")
    public ResponseEntity<ApiResponse<WishlistResponse>> addProduct(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable String productId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Added to wishlist",
                wishlistService.addProduct(currentUser.getUserId(), productId)));
    }

    @DeleteMapping("/me/products/{productId}")
    public ResponseEntity<ApiResponse<WishlistResponse>> removeProduct(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable String productId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Removed from wishlist",
                wishlistService.removeProduct(currentUser.getUserId(), productId)));
    }

    @PostMapping("/me/products/{productId}/move-to-cart")
    public ResponseEntity<ApiResponse<CartSummaryResponse>> moveToCart(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable String productId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Moved to cart",
                wishlistService.moveToCart(currentUser.getUserId(), productId)));
    }
}
