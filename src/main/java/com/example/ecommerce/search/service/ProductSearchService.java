package com.example.ecommerce.search.service;

import com.example.ecommerce.product.dto.ProductResponse;
import com.example.ecommerce.product.model.Product;
import com.example.ecommerce.search.dto.PagedResponse;
import com.example.ecommerce.search.dto.ProductSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("price", "averageRating", "createdAt", "name");

    private final MongoTemplate mongoTemplate;

    public PagedResponse<ProductResponse> search(ProductSearchCriteria criteria) {
        int safePage = Math.max(0, criteria.page());
        int safeSize = Math.min(Math.max(1, criteria.size()), 100);

        Query query = ProductSpecification.buildQuery(criteria);
        long totalElements = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class);

        String sortField = (criteria.sortBy() != null && ALLOWED_SORT_FIELDS.contains(criteria.sortBy()))
                ? criteria.sortBy()
                : "createdAt";
        Sort.Direction direction = "asc".equalsIgnoreCase(criteria.sortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        query.with(PageRequest.of(safePage, safeSize, Sort.by(direction, sortField)));
        List<ProductResponse> content = mongoTemplate.find(query, Product.class).stream()
                .map(ProductResponse::from)
                .toList();

        int totalPages = (int) Math.ceil((double) totalElements / safeSize);
        return new PagedResponse<>(content, safePage, safeSize, totalElements, totalPages);
    }
}
