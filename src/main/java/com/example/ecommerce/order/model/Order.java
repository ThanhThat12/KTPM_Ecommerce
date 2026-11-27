package com.example.ecommerce.order.model;

import com.example.ecommerce.common.model.BaseDocument;
import com.example.ecommerce.payment.model.PaymentMethod;
import com.example.ecommerce.user.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order extends BaseDocument {

    private String userId;

    private String userEmail;

    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    private BigDecimal subtotal;

    private BigDecimal totalAmount;

    private String appliedVoucherCode;

    private List<String> priceBreakdown;

    private PaymentMethod paymentMethod;

    private String paymentTransactionRef;

    private Address shippingAddress;

    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    private String cancelReason;
}
