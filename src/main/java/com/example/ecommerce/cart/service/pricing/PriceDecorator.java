package com.example.ecommerce.cart.service.pricing;

public abstract class PriceDecorator implements PriceCalculator {

    protected final PriceCalculator wrapped;

    protected PriceDecorator(PriceCalculator wrapped) {
        this.wrapped = wrapped;
    }
}
