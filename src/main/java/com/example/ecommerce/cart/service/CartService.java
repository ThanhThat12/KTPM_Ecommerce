package com.example.ecommerce.cart.service;

import com.example.ecommerce.cart.dto.AddCartItemRequest;
import com.example.ecommerce.cart.dto.CartSummaryResponse;
import com.example.ecommerce.cart.dto.CreateVoucherRequest;
import com.example.ecommerce.cart.dto.UpdateCartItemRequest;
import com.example.ecommerce.cart.model.Cart;
import com.example.ecommerce.cart.model.CartItem;
import com.example.ecommerce.cart.model.DiscountType;
import com.example.ecommerce.cart.model.Voucher;
import com.example.ecommerce.cart.repository.CartRepository;
import com.example.ecommerce.cart.repository.VoucherRepository;
import com.example.ecommerce.cart.service.pricing.BasePriceCalculator;
import com.example.ecommerce.cart.service.pricing.DiscountStrategy;
import com.example.ecommerce.cart.service.pricing.FixedAmountDiscountStrategy;
import com.example.ecommerce.cart.service.pricing.PercentageDiscountStrategy;
import com.example.ecommerce.cart.service.pricing.PriceCalculator;
import com.example.ecommerce.cart.service.pricing.ShippingFeeDecorator;
import com.example.ecommerce.cart.service.pricing.VipDiscountDecorator;
import com.example.ecommerce.cart.service.pricing.VoucherDiscountDecorator;
import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.product.model.Product;
import com.example.ecommerce.product.service.ProductService;
import com.example.ecommerce.user.model.User;
import com.example.ecommerce.user.model.UserRole;
import com.example.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final VoucherRepository voucherRepository;
    private final ProductService productService;
    private final UserService userService;

    public Cart getOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder().userId(userId).build()));
    }

    public CartSummaryResponse getCartSummary(String userId, String voucherCode) {
        Cart cart = getOrCreateCart(userId);
        return buildSummary(cart, voucherCode);
    }

    public CartSummaryResponse addItem(String userId, AddCartItemRequest request) {
        if (request == null || request.productId() == null || request.quantity() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Product ID and positive quantity are required");
        }
        Product product = productService.getEntityById(request.productId());
        if (!productService.hasSufficientStock(product.getId(), request.quantity())) {
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }

        Cart cart = getOrCreateCart(userId);
        Optional<CartItem> existing = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            int newQty = item.getQuantity() + request.quantity();
            if (!productService.hasSufficientStock(product.getId(), newQty)) {
                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
            }
            item.setQuantity(newQty);
            item.setUnitPrice(product.getPrice());
        } else {
            cart.getItems().add(CartItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .unitPrice(product.getPrice())
                    .quantity(request.quantity())
                    .build());
        }

        Cart saved = cartRepository.save(cart);
        return buildSummary(saved, null);
    }

    public CartSummaryResponse updateItem(String userId, String productId, UpdateCartItemRequest request) {
        if (request == null || request.quantity() < 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Quantity must be non-negative");
        }
        Cart cart = getOrCreateCart(userId);
        if (request.quantity() == 0) {
            cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        } else {
            if (!productService.hasSufficientStock(productId, request.quantity())) {
                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
            }
            CartItem item = cart.getItems().stream()
                    .filter(i -> i.getProductId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Item not in cart"));
            item.setQuantity(request.quantity());
        }
        return buildSummary(cartRepository.save(cart), null);
    }

    public CartSummaryResponse removeItem(String userId, String productId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return buildSummary(cartRepository.save(cart), null);
    }

    public void clearCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public Voucher createVoucher(CreateVoucherRequest request) {
        if (request == null || request.code() == null || request.code().isBlank()
                || request.discountType() == null || request.discountValue() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Voucher code, type, and value are required");
        }
        String normalizedCode = request.code().trim().toUpperCase();
        if (voucherRepository.existsByCodeIgnoreCase(normalizedCode)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Voucher code already exists");
        }
        Voucher voucher = Voucher.builder()
                .code(normalizedCode)
                .discountType(request.discountType())
                .discountValue(request.discountValue())
                .minOrderAmount(request.minOrderAmount() != null ? request.minOrderAmount() : BigDecimal.ZERO)
                .maxDiscountAmount(request.maxDiscountAmount())
                .expiresAt(request.expiresAt())
                .active(true)
                .build();
        return voucherRepository.save(voucher);
    }

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Voucher validateVoucher(String voucherCode, BigDecimal subtotal) {
        Voucher voucher = voucherRepository.findByCodeIgnoreCase(voucherCode.trim())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_VOUCHER));

        if (!voucher.isActive()) {
            throw new BusinessException(ErrorCode.INVALID_VOUCHER, "Voucher is inactive");
        }
        if (voucher.getExpiresAt() != null && voucher.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.INVALID_VOUCHER, "Voucher has expired");
        }
        if (voucher.getMinOrderAmount() != null && subtotal.compareTo(voucher.getMinOrderAmount()) < 0) {
            throw new BusinessException(
                    ErrorCode.INVALID_VOUCHER,
                    "Order amount does not meet minimum requirement of " + voucher.getMinOrderAmount()
            );
        }
        return voucher;
    }

    public CartSummaryResponse buildSummary(Cart cart, String voucherCode) {
        BigDecimal subtotal = cart.calculateSubtotal();
        PriceCalculator calculator = new BasePriceCalculator(subtotal);

        String appliedCode = null;
        if (voucherCode != null && !voucherCode.isBlank()) {
            Voucher voucher = validateVoucher(voucherCode, subtotal);
            DiscountStrategy strategy = resolveStrategy(voucher.getDiscountType());
            calculator = new VoucherDiscountDecorator(calculator, voucher, strategy);
            appliedCode = voucher.getCode();
        }

        User user = userService.getEntityById(cart.getUserId());
        if (user.getRole() == UserRole.VIP) {
            calculator = new VipDiscountDecorator(calculator);
        }

        if (!cart.getItems().isEmpty()) {
            calculator = new ShippingFeeDecorator(calculator);
        }

        return new CartSummaryResponse(
                cart.getUserId(),
                cart.getItems(),
                subtotal,
                calculator.calculate(),
                appliedCode,
                calculator.describeBreakdown()
        );
    }

    private DiscountStrategy resolveStrategy(DiscountType discountType) {
        return switch (discountType) {
            case PERCENTAGE -> new PercentageDiscountStrategy();
            case FIXED_AMOUNT -> new FixedAmountDiscountStrategy();
        };
    }
}
