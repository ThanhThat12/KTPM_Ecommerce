package com.example.ecommerce.product.service;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.product.dto.CategoryRequest;
import com.example.ecommerce.product.dto.ProductRequest;
import com.example.ecommerce.product.dto.ProductResponse;
import com.example.ecommerce.product.model.Category;
import com.example.ecommerce.product.model.Product;
import com.example.ecommerce.product.repository.CategoryRepository;
import com.example.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Category createCategory(CategoryRequest request) {
        if (request == null || isBlank(request.slug()) || isBlank(request.name())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Category slug and name are required");
        }
        if (categoryRepository.existsBySlug(request.slug().trim().toLowerCase())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Category slug already exists");
        }
        Category category = Category.builder()
                .slug(request.slug().trim().toLowerCase())
                .name(request.name().trim())
                .description(request.description())
                .build();
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public ProductResponse createProduct(ProductRequest request) {
        validateProductRequest(request);
        if (productRepository.existsBySku(request.sku().trim())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Product SKU already exists");
        }

        Product product = Product.builder()
                .sku(request.sku().trim())
                .name(request.name().trim())
                .description(request.description())
                .categoryId(request.categoryId())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .imageUrls(request.imageUrls() != null ? new ArrayList<>(request.imageUrls()) : new ArrayList<>())
                .attributes(request.attributes() != null ? new HashMap<>(request.attributes()) : new HashMap<>())
                .active(true)
                .build();

        return ProductResponse.from(productRepository.save(product));
    }

    public Product getEntityById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public ProductResponse getById(String productId) {
        return ProductResponse.from(getEntityById(productId));
    }

    public List<ProductResponse> getAll(String categoryId, String keyword, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products;
        if (categoryId != null && !categoryId.isBlank()) {
            products = productRepository.findByCategoryIdAndActiveTrue(categoryId);
        } else if (keyword != null && !keyword.isBlank()) {
            products = productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword.trim());
        } else if (minPrice != null && maxPrice != null) {
            products = productRepository.findByPriceBetweenAndActiveTrue(minPrice, maxPrice);
        } else {
            products = productRepository.findByActiveTrue();
        }
        return products.stream().map(ProductResponse::from).toList();
    }

    public ProductResponse updateProduct(String productId, ProductRequest request) {
        validateProductRequest(request);
        Product product = getEntityById(productId);
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setCategoryId(request.categoryId());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        if (request.imageUrls() != null) {
            product.setImageUrls(new ArrayList<>(request.imageUrls()));
        }
        if (request.attributes() != null) {
            product.setAttributes(new HashMap<>(request.attributes()));
        }
        return ProductResponse.from(productRepository.save(product));
    }

    public void deleteProduct(String productId) {
        Product product = getEntityById(productId);
        product.setActive(false);
        productRepository.save(product);
    }

    public boolean hasSufficientStock(String productId, int requestedQuantity) {
        Product product = getEntityById(productId);
        return product.isActive() && product.getStockQuantity() >= requestedQuantity;
    }

    public void deductStock(String productId, int quantity) {
        Product product = getEntityById(productId);
        if (product.getStockQuantity() < quantity) {
            throw new BusinessException(
                    ErrorCode.OUT_OF_STOCK,
                    "Insufficient stock for product " + product.getName()
            );
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    public void restoreStock(String productId, int quantity) {
        Product product = getEntityById(productId);
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);
    }

    private void validateProductRequest(ProductRequest request) {
        if (request == null || isBlank(request.sku()) || isBlank(request.name())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "SKU and product name are required");
        }
        if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Price must be non-negative");
        }
        if (request.stockQuantity() < 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Stock quantity must be non-negative");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
