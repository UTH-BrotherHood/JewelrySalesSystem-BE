package com.example.JewelrySalesSystem.dto.request.ProductRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    @NotBlank(message = "Category ID is required")
    String categoryId;
    String name;
    String description;

    String imageUrl;
    BigDecimal costPrice;
    BigDecimal weight;
    BigDecimal laborCost;
    BigDecimal stoneCost;
    Integer stockQuantity;
}