package com.example.ecommerce.payment.service.factory;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.payment.model.PaymentMethod;
import com.example.ecommerce.payment.service.adapter.PaymentGatewayAdapter;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Factory Method Pattern: resolves the concrete PaymentGatewayAdapter for a given PaymentMethod.
 */
@Component
public class PaymentGatewayFactory {

    private final Map<PaymentMethod, PaymentGatewayAdapter> adapters = new EnumMap<>(PaymentMethod.class);

    public PaymentGatewayFactory(List<PaymentGatewayAdapter> adapterList) {
        for (PaymentGatewayAdapter adapter : adapterList) {
            adapters.put(adapter.supportedMethod(), adapter);
        }
    }

    public PaymentGatewayAdapter getAdapter(PaymentMethod method) {
        PaymentGatewayAdapter adapter = adapters.get(method);
        if (adapter == null) {
            throw new BusinessException(ErrorCode.UNSUPPORTED_PAYMENT_METHOD);
        }
        return adapter;
    }
}
