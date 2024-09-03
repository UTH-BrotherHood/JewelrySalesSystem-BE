package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.PromotionRequests.PromotionCreationRequest;
import com.example.JewelrySalesSystem.dto.response.PromotionResponse;
import com.example.JewelrySalesSystem.entity.Promotion;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.PromotionMapper;
import com.example.JewelrySalesSystem.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionResponse createPromotion(PromotionCreationRequest request) {
        if (promotionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PROMOTION_ALREADY_EXISTS);
        }

        String promotionCode = "PROMO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Promotion promotion = promotionMapper.toPromotion(request, promotionCode);
        Promotion savedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toPromotionResponse(savedPromotion);
    }

    public PromotionResponse getPromotionByCode(String promotionCode) {
        Promotion promotion = promotionRepository.findByPromotionCode(promotionCode)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
        return promotionMapper.toPromotionResponse(promotion);
    }
}
