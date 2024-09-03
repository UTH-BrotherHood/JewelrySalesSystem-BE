package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.PromotionRequests.PromotionCreationRequest;
import com.example.JewelrySalesSystem.dto.response.PromotionResponse;
import com.example.JewelrySalesSystem.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PromotionMapper {

    @Mapping(target = "promotionCode", source = "promotionCode")
    Promotion toPromotion(PromotionCreationRequest request, String promotionCode);

    PromotionResponse toPromotionResponse(Promotion promotion);
}
