package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.PromotionRequests.PromotionCreationRequest;
import com.example.JewelrySalesSystem.dto.request.PromotionRequests.PromotionUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.PromotionResponse;
import com.example.JewelrySalesSystem.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PromotionResponse>> createPromotion(@RequestBody PromotionCreationRequest request) {
        PromotionResponse response = promotionService.createPromotion(request);
        ApiResponse<PromotionResponse> apiResponse = new ApiResponse<>(201, "Promotion created successfully", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{promotionId}")
    public ResponseEntity<ApiResponse<PromotionResponse>> updatePromotion(
            @PathVariable String promotionId,
            @RequestBody PromotionUpdateRequest request) {
        PromotionResponse response = promotionService.updatePromotion(promotionId, request);
        ApiResponse<PromotionResponse> apiResponse = new ApiResponse<>(200, "Promotion updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{promotionId}")
    public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable String promotionId) {
        promotionService.deletePromotion(promotionId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Promotion deleted successfully", null);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{promotionId}")
    public ResponseEntity<ApiResponse<PromotionResponse>> getPromotion(@PathVariable String promotionId) {
        PromotionResponse response = promotionService.getPromotion(promotionId);
        ApiResponse<PromotionResponse> apiResponse = new ApiResponse<>(200, "Promotion retrieved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PromotionResponse>>> getPromotions(Pageable pageable) {
        Page<PromotionResponse> promotions = promotionService.getPromotions(pageable);
        ApiResponse<Page<PromotionResponse>> apiResponse = new ApiResponse<>(200, "Promotions retrieved successfully", promotions);
        return ResponseEntity.ok(apiResponse);
    }
}
