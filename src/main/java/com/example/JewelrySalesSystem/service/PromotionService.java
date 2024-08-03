package com.example.JewelrySalesSystem.service;
import com.example.JewelrySalesSystem.dto.request.PromotionCreationRequest;
import com.example.JewelrySalesSystem.dto.request.PromotionUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.PromotionResponse;
import com.example.JewelrySalesSystem.entity.Promotion;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.PromotionMapper;
import com.example.JewelrySalesSystem.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionResponse createPromotion(PromotionCreationRequest request) {
        Promotion promotion = promotionMapper.toPromotion(request);
        Promotion savedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toPromotionResponse(savedPromotion);
    }

    public PromotionResponse updatePromotion(Integer promotionId, PromotionUpdateRequest request) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
        promotionMapper.updatePromotion(promotion, request);
        Promotion updatedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toPromotionResponse(updatedPromotion);
    }

    public void deletePromotion(Integer promotionId) {
        if (!promotionRepository.existsById(promotionId)) {
            throw new AppException(ErrorCode.PROMOTION_NOT_FOUND);
        }
        promotionRepository.deleteById(promotionId);
    }

    public PromotionResponse getPromotion(Integer promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
        return promotionMapper.toPromotionResponse(promotion);
    }

    public Page<PromotionResponse> getPromotions(Pageable pageable) {
        return promotionRepository.findAll(pageable)
                .map(promotionMapper::toPromotionResponse);
    }
}
