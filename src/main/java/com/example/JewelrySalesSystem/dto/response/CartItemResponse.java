package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
     String itemId;
     String productId;
     String productImageUrl;
     String productName;
     Integer quantity;
     BigDecimal price;
     BigDecimal totalPrice;
}