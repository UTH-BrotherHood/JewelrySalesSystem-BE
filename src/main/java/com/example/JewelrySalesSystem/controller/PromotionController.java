package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.PromotionCreationRequest;
import com.example.JewelrySalesSystem.dto.request.PromotionUpdateRequest;
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
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody PromotionCreationRequest request) {
        return new ResponseEntity<>(promotionService.createPromotion(request), HttpStatus.CREATED);
    }

    @PutMapping("/{promotionId}")
    public ResponseEntity<PromotionResponse> updatePromotion(
            @PathVariable Integer promotionId,
            @RequestBody PromotionUpdateRequest request) {
        return ResponseEntity.ok(promotionService.updatePromotion(promotionId, request));
    }

    @DeleteMapping("/{promotionId}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Integer promotionId) {
        promotionService.deletePromotion(promotionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{promotionId}")
    public ResponseEntity<PromotionResponse> getPromotion(@PathVariable Integer promotionId) {
        return ResponseEntity.ok(promotionService.getPromotion(promotionId));
    }

    @GetMapping
    public ResponseEntity<Page<PromotionResponse>> getPromotions(Pageable pageable) {
        return ResponseEntity.ok(promotionService.getPromotions(pageable));
    }
}
