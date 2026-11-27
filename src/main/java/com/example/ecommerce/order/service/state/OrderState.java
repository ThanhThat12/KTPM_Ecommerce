package com.example.ecommerce.order.service.state;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.order.model.Order;
import com.example.ecommerce.order.model.OrderStatus;

/**
 * State Pattern: encapsulates valid order lifecycle transitions.
 */
public interface OrderState {

    OrderStatus status();

    default void pay(Order order) {
        throw invalidTransition("PAY");
    }

    default void ship(Order order) {
        throw invalidTransition("SHIP");
    }

    default void deliver(Order order) {
        throw invalidTransition("DELIVER");
    }

    default void cancel(Order order, String reason) {
        throw invalidTransition("CANCEL");
    }

    private BusinessException invalidTransition(String action) {
        return new BusinessException(
                ErrorCode.INVALID_ORDER_STATE,
                "Cannot perform action " + action + " when order is in state " + status()
        );
    }
}
