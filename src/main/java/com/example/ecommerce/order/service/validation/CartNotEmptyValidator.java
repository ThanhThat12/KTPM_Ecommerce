package com.example.ecommerce.order.service.validation;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;

public class CartNotEmptyValidator extends CheckoutValidationHandler {

    @Override
    protected void doValidate(CheckoutContext context) {
        if (context.cart() == null || context.cart().getItems().isEmpty()) {
            throw new BusinessException(ErrorCode.CART_EMPTY);
        }
    }
}
