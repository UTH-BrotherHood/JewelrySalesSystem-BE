package com.example.JewelrySalesSystem.dto.response;

import com.example.JewelrySalesSystem.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String productId;
    String categoryId;
    String name;
    String description;
    BigDecimal costPrice;
    BigDecimal weight;
    BigDecimal laborCost;
    BigDecimal stoneCost;
    Integer stockQuantity;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public ProductResponse(Product product) {
        this.productId = product.getProductId();
        this.categoryId = product.getCategoryId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.costPrice = product.getCostPrice();
        this.weight = product.getWeight();
        this.laborCost = product.getLaborCost();
        this.stoneCost = product.getStoneCost();
        this.stockQuantity = product.getStockQuantity();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }
}
