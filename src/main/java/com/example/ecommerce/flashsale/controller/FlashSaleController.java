package com.example.ecommerce.flashsale.controller;

import com.example.ecommerce.common.response.ApiResponse;
import com.example.ecommerce.flashsale.dto.CreateFlashSaleRequest;
import com.example.ecommerce.flashsale.dto.FlashSaleResponse;
import com.example.ecommerce.flashsale.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flash-sales")
@RequiredArgsConstructor
public class FlashSaleController {

    private final FlashSaleService flashSaleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FlashSaleResponse>>> getActiveSales() {
        return ResponseEntity.ok(ApiResponse.ok(flashSaleService.getActiveFlashSales()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FlashSaleResponse>> create(@RequestBody CreateFlashSaleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Flash sale created", flashSaleService.create(request)));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<FlashSaleResponse>> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok("Flash sale deactivated", flashSaleService.deactivate(id)));
    }
}
