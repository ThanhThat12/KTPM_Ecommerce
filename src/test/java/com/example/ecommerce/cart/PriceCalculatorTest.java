package com.example.ecommerce.cart;

import com.example.ecommerce.cart.model.DiscountType;
import com.example.ecommerce.cart.model.Voucher;
import com.example.ecommerce.cart.service.pricing.BasePriceCalculator;
import com.example.ecommerce.cart.service.pricing.PercentageDiscountStrategy;
import com.example.ecommerce.cart.service.pricing.PriceCalculator;
import com.example.ecommerce.cart.service.pricing.ShippingFeeDecorator;
import com.example.ecommerce.cart.service.pricing.VipDiscountDecorator;
import com.example.ecommerce.cart.service.pricing.VoucherDiscountDecorator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceCalculatorTest {

    @Test
    void shouldApplyVoucherVipAndFreeShippingDecoratorsInOrder() {
        Voucher voucher = Voucher.builder()
                .code("SALE10")
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(new BigDecimal("10"))
                .build();

        PriceCalculator calculator = new BasePriceCalculator(new BigDecimal("1000000"));
        calculator = new VoucherDiscountDecorator(calculator, voucher, new PercentageDiscountStrategy());
        calculator = new VipDiscountDecorator(calculator);
        calculator = new ShippingFeeDecorator(calculator);

        // 1,000,000 - 10% = 900,000; 900,000 - 5% VIP = 855,000; >= 500,000 => FREE shipping
        assertEquals(0, new BigDecimal("855000.00").compareTo(calculator.calculate()));
        assertTrue(calculator.describeBreakdown().stream().anyMatch(line -> line.contains("FREE")));
    }

    @Test
    void shouldChargeShippingFeeBelowThreshold() {
        PriceCalculator calculator = new ShippingFeeDecorator(new BasePriceCalculator(new BigDecimal("200000")));

        assertEquals(0, new BigDecimal("230000").compareTo(calculator.calculate()));
    }
}
