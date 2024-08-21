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
    public ApiResponse<PromotionResponse> createPromotion(@RequestBody PromotionCreationRequest request) {
        ApiResponse<PromotionResponse> apiResponse = new ApiResponse<>();
        PromotionResponse response = promotionService.createPromotion(request);
        apiResponse.setCode(201);
        apiResponse.setMessage("Promotion created successfully");
        apiResponse.setResult(response);
        return apiResponse;
    }

    @PutMapping("/{promotionId}")
    public ApiResponse<PromotionResponse> updatePromotion(
            @PathVariable String promotionId,
            @RequestBody PromotionUpdateRequest request) {
        ApiResponse<PromotionResponse> apiResponse = new ApiResponse<>();
        PromotionResponse response = promotionService.updatePromotion(promotionId, request);
        apiResponse.setCode(200);
        apiResponse.setMessage("Promotion updated successfully");
        apiResponse.setResult(response);
        return apiResponse;
    }

    @DeleteMapping("/{promotionId}")
    public ApiResponse<String> deletePromotion(@PathVariable String promotionId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        promotionService.deletePromotion(promotionId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Promotion deleted successfully");
        apiResponse.setResult("Promotion has been deleted");
        return apiResponse;
    }

    @GetMapping("/{promotionId}")
    public ApiResponse<PromotionResponse> getPromotion(@PathVariable String promotionId) {
        ApiResponse<PromotionResponse> apiResponse = new ApiResponse<>();
        PromotionResponse response = promotionService.getPromotion(promotionId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Promotion retrieved successfully");
        apiResponse.setResult(response);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<Page<PromotionResponse>> getPromotions(Pageable pageable) {
        ApiResponse<Page<PromotionResponse>> apiResponse = new ApiResponse<>();
        Page<PromotionResponse> promotions = promotionService.getPromotions(pageable);
        apiResponse.setCode(200);
        apiResponse.setMessage("Promotions retrieved successfully");
        apiResponse.setResult(promotions);
        return apiResponse;
    }


}
