package com.example.JewelrySalesSystem.dto.response;

import com.example.JewelrySalesSystem.entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String productId;
    String categoryId;
    String name;
    String description;
    BigDecimal costPrice;
    BigDecimal weight;
    BigDecimal laborCost;
    String imageUrl;
    BigDecimal stoneCost;
    Integer stockQuantity;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
