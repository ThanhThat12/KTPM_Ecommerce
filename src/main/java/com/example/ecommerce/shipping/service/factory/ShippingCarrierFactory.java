package com.example.ecommerce.shipping.service.factory;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.shipping.model.ShippingCarrier;
import com.example.ecommerce.shipping.service.adapter.ShippingCarrierAdapter;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Factory Method Pattern: resolves the ShippingCarrierAdapter for a carrier.
 */
@Component
public class ShippingCarrierFactory {

    private final Map<ShippingCarrier, ShippingCarrierAdapter> adapters = new EnumMap<>(ShippingCarrier.class);

    public ShippingCarrierFactory(List<ShippingCarrierAdapter> adapterList) {
        for (ShippingCarrierAdapter adapter : adapterList) {
            adapters.put(adapter.carrier(), adapter);
        }
    }

    public ShippingCarrierAdapter getAdapter(ShippingCarrier carrier) {
        ShippingCarrierAdapter adapter = adapters.get(carrier);
        if (adapter == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Unsupported shipping carrier: " + carrier);
        }
        return adapter;
    }

    public List<ShippingCarrierAdapter> getAllAdapters() {
        return List.copyOf(adapters.values());
    }
}
