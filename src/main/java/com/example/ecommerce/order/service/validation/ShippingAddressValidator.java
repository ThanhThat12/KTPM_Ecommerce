package com.example.ecommerce.order.service.validation;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.user.model.Address;

public class ShippingAddressValidator extends CheckoutValidationHandler {

    @Override
    protected void doValidate(CheckoutContext context) {
        Address address = context.request().shippingAddress();
        if (address == null
                || address.getStreet() == null || address.getStreet().isBlank()
                || address.getCity() == null || address.getCity().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Valid shipping address is required");
        }
        if (context.request().paymentMethod() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Payment method is required");
        }
    }
}
