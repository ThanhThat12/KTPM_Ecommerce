package com.example.ecommerce.cart.controller;

import com.example.ecommerce.cart.dto.AddCartItemRequest;
import com.example.ecommerce.cart.dto.CartSummaryResponse;
import com.example.ecommerce.cart.dto.CreateVoucherRequest;
import com.example.ecommerce.cart.dto.UpdateCartItemRequest;
import com.example.ecommerce.cart.model.Voucher;
import com.example.ecommerce.cart.service.CartService;
import com.example.ecommerce.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class CartController {

    private final CartService cartService;

    @GetMapping("/carts/{userId}")
    public ResponseEntity<ApiResponse<CartSummaryResponse>> getCart(
            @PathVariable String userId,
            @RequestParam(required = false) String voucherCode
    ) {
        return ResponseEntity.ok(ApiResponse.ok(cartService.getCartSummary(userId, voucherCode)));
    }

    @PostMapping("/carts/{userId}/items")
    public ResponseEntity<ApiResponse<CartSummaryResponse>> addItem(
            @PathVariable String userId,
            @RequestBody AddCartItemRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Item added to cart", cartService.addItem(userId, request)));
    }

    @PutMapping("/carts/{userId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartSummaryResponse>> updateItem(
            @PathVariable String userId,
            @PathVariable String productId,
            @RequestBody UpdateCartItemRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Cart updated", cartService.updateItem(userId, productId, request)));
    }

    @DeleteMapping("/carts/{userId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartSummaryResponse>> removeItem(
            @PathVariable String userId,
            @PathVariable String productId
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Item removed", cartService.removeItem(userId, productId)));
    }

    @DeleteMapping("/carts/{userId}")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.ok("Cart cleared", null));
    }

    @PostMapping("/vouchers")
    public ResponseEntity<ApiResponse<Voucher>> createVoucher(@RequestBody CreateVoucherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Voucher created", cartService.createVoucher(request)));
    }

    @GetMapping("/vouchers")
    public ResponseEntity<ApiResponse<List<Voucher>>> getVouchers() {
        return ResponseEntity.ok(ApiResponse.ok(cartService.getAllVouchers()));
    }
}
