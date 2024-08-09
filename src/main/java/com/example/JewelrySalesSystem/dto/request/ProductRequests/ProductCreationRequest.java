package com.example.JewelrySalesSystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationRequest {
    String categoryId;
    String name;
    String description;
    BigDecimal costPrice;
    BigDecimal weight;
    BigDecimal laborCost;
    BigDecimal stoneCost;
    Integer stockQuantity;
}
