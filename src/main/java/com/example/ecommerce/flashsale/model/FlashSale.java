package com.example.ecommerce.flashsale.model;

import com.example.ecommerce.common.model.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flash_sales")
public class FlashSale extends BaseDocument {

    private String productId;

    private String productName;

    private BigDecimal originalPrice;

    private BigDecimal salePrice;

    private int maxQuantity;

    @Builder.Default
    private int soldQuantity = 0;

    private Instant startTime;

    private Instant endTime;

    @Builder.Default
    private boolean active = true;

    public boolean isRunningNow() {
        Instant now = Instant.now();
        return active
                && soldQuantity < maxQuantity
                && !now.isBefore(startTime)
                && now.isBefore(endTime);
    }
}
