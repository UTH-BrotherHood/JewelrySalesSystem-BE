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
public class ProductCreationRequest {
    @NotBlank(message = "Category id is required")
    String categoryId;

    @NotBlank(message = "Product name is required")
    String name;

    String description;

    @NotNull(message = "Cost price is required")
    BigDecimal costPrice;

    @NotNull(message = "Weight is required")
    BigDecimal weight;

    @NotNull(message = "Labor cost is required")
    BigDecimal laborCost;

    @NotNull(message = "Stone cost is required")
    BigDecimal stoneCost;

    @NotNull(message = "Stock quantity is required")
    Integer stockQuantity;
}
