package com.example.ecommerce.product.model;

import com.example.ecommerce.common.model.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Product document constructed via the Builder pattern.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product extends BaseDocument {

    @Indexed(unique = true)
    private String sku;

    private String name;

    private String description;

    private String categoryId;

    private BigDecimal price;

    private int stockQuantity;

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @Builder.Default
    private Map<String, String> attributes = new HashMap<>();

    @Builder.Default
    private boolean active = true;
}
