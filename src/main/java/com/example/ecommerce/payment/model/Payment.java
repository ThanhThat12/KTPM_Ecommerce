package com.example.ecommerce.payment.model;

import com.example.ecommerce.common.model.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment extends BaseDocument {

    private String orderId;

    private String userId;

    private BigDecimal amount;

    private PaymentMethod method;

    private PaymentStatus status;

    private String transactionRef;

    private String gatewayMessage;
}
