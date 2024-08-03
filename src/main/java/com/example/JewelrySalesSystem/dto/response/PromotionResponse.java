package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionResponse {
    Integer promotionId;
    String promotionDescription;
    LocalDateTime startDate;
    LocalDateTime endDate;
    BigDecimal discountPercentage;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
