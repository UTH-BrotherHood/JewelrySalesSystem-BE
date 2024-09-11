package com.example.JewelrySalesSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {
    @NotNull(message = "Product ID is required")
    String productId;

    @NotNull(message = "Quantity is required")
    Integer quantity;


}
