package com.example.JewelrySalesSystem.dto.request.WarrantyRequests;

import com.example.JewelrySalesSystem.validator.GenericConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarrantyCreationRequest {

    @NotNull(message = "Product ID is required")
    String productId;

    @NotNull(message = "Warranty start date is required")
    LocalDateTime warrantyStartDate;

    @NotNull(message = "Warranty end date is required")
    LocalDateTime warrantyEndDate;
}
