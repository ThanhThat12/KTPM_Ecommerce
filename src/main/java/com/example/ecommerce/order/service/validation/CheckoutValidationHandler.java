package com.example.ecommerce.order.service.validation;

/**
 * Chain of Responsibility Pattern: base handler for checkout pre-condition checks.
 */
public abstract class CheckoutValidationHandler {

    private CheckoutValidationHandler next;

    public CheckoutValidationHandler linkWith(CheckoutValidationHandler next) {
        this.next = next;
        return next;
    }

    public void validate(CheckoutContext context) {
        doValidate(context);
        if (next != null) {
            next.validate(context);
        }
    }

    protected abstract void doValidate(CheckoutContext context);
}
