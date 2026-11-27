package com.example.ecommerce.order.service.state;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.order.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderStateResolver {

    private final Map<OrderStatus, OrderState> states = new EnumMap<>(OrderStatus.class);

    public OrderStateResolver(List<OrderState> stateList) {
        for (OrderState state : stateList) {
            states.put(state.status(), state);
        }
    }

    public OrderState resolve(OrderStatus status) {
        OrderState state = states.get(status);
        if (state == null) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATE, "Unknown order status: " + status);
        }
        return state;
    }
}
