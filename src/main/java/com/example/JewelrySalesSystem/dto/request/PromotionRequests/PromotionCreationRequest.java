package com.example.JewelrySalesSystem.dto.request.PromotionRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionCreationRequest {
    @NotBlank(message = "PROMOTION_DESCRIPTION_REQUIRED")
    String promotionDescription;

    @NotNull(message = "START_DATE_REQUIRED")
    LocalDateTime startDate;

    @NotNull(message = "END_DATE_REQUIRED")
    LocalDateTime endDate;

    @NotNull(message = "DISCOUNT_PERCENTAGE_REQUIRED")
    @Positive(message = "DISCOUNT_PERCENTAGE_INVALID")
    BigDecimal discountPercentage;
}


