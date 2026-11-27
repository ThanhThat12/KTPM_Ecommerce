package com.example.ecommerce.flashsale.service;

import com.example.ecommerce.common.exception.BusinessException;
import com.example.ecommerce.common.exception.ErrorCode;
import com.example.ecommerce.flashsale.dto.CreateFlashSaleRequest;
import com.example.ecommerce.flashsale.dto.FlashSaleResponse;
import com.example.ecommerce.flashsale.model.FlashSale;
import com.example.ecommerce.flashsale.repository.FlashSaleRepository;
import com.example.ecommerce.product.model.Product;
import com.example.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashSaleService {

    private final FlashSaleRepository flashSaleRepository;
    private final ProductService productService;

    public FlashSaleResponse create(CreateFlashSaleRequest request) {
        if (request == null || request.productId() == null || request.salePrice() == null
                || request.startTime() == null || request.endTime() == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Product, sale price, start and end time are required");
        }
        if (!request.endTime().isAfter(request.startTime())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "End time must be after start time");
        }
        if (request.maxQuantity() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Max quantity must be positive");
        }

        Product product = productService.getEntityById(request.productId());
        if (request.salePrice().compareTo(BigDecimal.ZERO) <= 0
                || request.salePrice().compareTo(product.getPrice()) >= 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Sale price must be positive and lower than original price");
        }

        FlashSale sale = FlashSale.builder()
                .productId(product.getId())
                .productName(product.getName())
                .originalPrice(product.getPrice())
                .salePrice(request.salePrice())
                .maxQuantity(request.maxQuantity())
                .soldQuantity(0)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .active(true)
                .build();

        return FlashSaleResponse.from(flashSaleRepository.save(sale));
    }

    public List<FlashSaleResponse> getActiveFlashSales() {
        return flashSaleRepository.findByActiveTrue().stream()
                .filter(FlashSale::isRunningNow)
                .map(FlashSaleResponse::from)
                .toList();
    }

    public Optional<FlashSale> getRunningSaleForProduct(String productId) {
        return flashSaleRepository.findByProductIdAndActiveTrue(productId).stream()
                .filter(FlashSale::isRunningNow)
                .findFirst();
    }

    public FlashSaleResponse deactivate(String flashSaleId) {
        FlashSale sale = flashSaleRepository.findById(flashSaleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Flash sale not found"));
        sale.setActive(false);
        return FlashSaleResponse.from(flashSaleRepository.save(sale));
    }

    @Scheduled(fixedRate = 60000)
    public void deactivateExpiredSales() {
        List<FlashSale> expired = flashSaleRepository.findByActiveTrueAndEndTimeBefore(Instant.now());
        if (!expired.isEmpty()) {
            expired.forEach(sale -> sale.setActive(false));
            flashSaleRepository.saveAll(expired);
            log.info("Deactivated {} expired flash sales", expired.size());
        }
    }
}
