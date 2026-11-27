package com.example.ecommerce.search.service;

import com.example.ecommerce.search.dto.ProductSearchCriteria;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Specification / Criteria Pattern: dynamically composes MongoDB search filters.
 */
public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Query buildQuery(ProductSearchCriteria criteria) {
        List<Criteria> filters = new ArrayList<>();
        filters.add(Criteria.where("active").is(true));

        if (criteria.keyword() != null && !criteria.keyword().isBlank()) {
            String escaped = Pattern.quote(criteria.keyword().trim());
            filters.add(new Criteria().orOperator(
                    Criteria.where("name").regex(escaped, "i"),
                    Criteria.where("description").regex(escaped, "i")
            ));
        }
        if (criteria.categoryId() != null && !criteria.categoryId().isBlank()) {
            filters.add(Criteria.where("categoryId").is(criteria.categoryId()));
        }
        if (criteria.minPrice() != null) {
            filters.add(Criteria.where("price").gte(criteria.minPrice()));
        }
        if (criteria.maxPrice() != null) {
            filters.add(Criteria.where("price").lte(criteria.maxPrice()));
        }
        if (criteria.minRating() != null) {
            filters.add(Criteria.where("averageRating").gte(criteria.minRating()));
        }
        if (Boolean.TRUE.equals(criteria.inStockOnly())) {
            filters.add(Criteria.where("stockQuantity").gt(0));
        }

        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        return query;
    }
}
