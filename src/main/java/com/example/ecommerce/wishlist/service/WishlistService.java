package com.example.ecommerce.wishlist.service;

import com.example.ecommerce.cart.dto.AddCartItemRequest;
import com.example.ecommerce.cart.dto.CartSummaryResponse;
import com.example.ecommerce.cart.service.CartService;
import com.example.ecommerce.product.dto.ProductResponse;
import com.example.ecommerce.product.service.ProductService;
import com.example.ecommerce.wishlist.dto.WishlistResponse;
import com.example.ecommerce.wishlist.model.Wishlist;
import com.example.ecommerce.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    private final CartService cartService;

    public Wishlist getOrCreate(String userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> wishlistRepository.save(Wishlist.builder().userId(userId).build()));
    }

    public WishlistResponse getWishlist(String userId) {
        return toResponse(getOrCreate(userId));
    }

    public WishlistResponse addProduct(String userId, String productId) {
        productService.getEntityById(productId);
        Wishlist wishlist = getOrCreate(userId);
        wishlist.getProductIds().add(productId);
        return toResponse(wishlistRepository.save(wishlist));
    }

    public WishlistResponse removeProduct(String userId, String productId) {
        Wishlist wishlist = getOrCreate(userId);
        wishlist.getProductIds().remove(productId);
        return toResponse(wishlistRepository.save(wishlist));
    }

    public CartSummaryResponse moveToCart(String userId, String productId) {
        Wishlist wishlist = getOrCreate(userId);
        wishlist.getProductIds().remove(productId);
        wishlistRepository.save(wishlist);
        return cartService.addItem(userId, new AddCartItemRequest(productId, 1));
    }

    private WishlistResponse toResponse(Wishlist wishlist) {
        List<ProductResponse> products = wishlist.getProductIds().stream()
                .map(productService::getById)
                .toList();
        return new WishlistResponse(wishlist.getUserId(), products);
    }
}
