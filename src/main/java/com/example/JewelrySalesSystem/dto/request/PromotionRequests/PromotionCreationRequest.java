package com.example.JewelrySalesSystem.dto.request.PromotionRequests;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionCreationRequest {
    String name;
    BigDecimal discountPercentage;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
