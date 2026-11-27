package com.example.ecommerce.order.service.validation;

import com.example.ecommerce.cart.model.CartItem;
import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StockAvailabilityValidator extends CheckoutValidationHandler {

    private final ProductService productService;

    @Override
    protected void doValidate(CheckoutContext context) {
        for (CartItem item : context.cart().getItems()) {
            if (!productService.hasSufficientStock(item.getProductId(), item.getQuantity())) {
                throw new BusinessException(
                        ErrorCode.OUT_OF_STOCK,
                        "Insufficient stock for product: " + item.getProductName()
                );
            }
        }
    }
}
