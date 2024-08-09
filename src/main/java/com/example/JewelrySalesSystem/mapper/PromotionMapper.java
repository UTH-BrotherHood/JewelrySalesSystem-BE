package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.PromotionRequests.PromotionCreationRequest;
import com.example.JewelrySalesSystem.dto.request.PromotionRequests.PromotionUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.PromotionResponse;
import com.example.JewelrySalesSystem.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    Promotion toPromotion(PromotionCreationRequest request);

    PromotionResponse toPromotionResponse(Promotion promotion);

    void updatePromotion(@MappingTarget Promotion promotion, PromotionUpdateRequest request);
}
