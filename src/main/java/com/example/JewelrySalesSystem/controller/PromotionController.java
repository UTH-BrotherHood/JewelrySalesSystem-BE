package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.PromotionRequests.PromotionCreationRequest;
import com.example.JewelrySalesSystem.dto.response.PromotionResponse;
import com.example.JewelrySalesSystem.service.PromotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionController {

    PromotionService promotionService;

    @PostMapping
    public ApiResponse<PromotionResponse> createPromotion(@RequestBody PromotionCreationRequest request) {
        ApiResponse<PromotionResponse> apiResponse = new ApiResponse<>();
        PromotionResponse promotion = promotionService.createPromotion(request);
        apiResponse.setCode(201);
        apiResponse.setMessage("Promotion created successfully");
        apiResponse.setResult(promotion);
        return apiResponse;
    }

    @GetMapping("/{promotionCode}")
    public ApiResponse<PromotionResponse> getPromotionByCode(@PathVariable String promotionCode) {
        ApiResponse<PromotionResponse> apiResponse = new ApiResponse<>();
        PromotionResponse promotion = promotionService.getPromotionByCode(promotionCode);
        apiResponse.setCode(200);
        apiResponse.setMessage("Promotion retrieved successfully");
        apiResponse.setResult(promotion);
        return apiResponse;
    }
}
