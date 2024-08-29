package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarrantyResponse {
    String warrantyId;
    String productId;
    LocalDateTime warrantyStartDate;
    LocalDateTime warrantyEndDate;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
