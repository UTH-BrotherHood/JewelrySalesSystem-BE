package com.example.JewelrySalesSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReturnPolicyRequest {
    @NotNull(message = "Description is required")
    String description;

    @NotNull(message = "Effective date is required")
    LocalDateTime effectiveDate;

    @NotNull(message = "Expiry date is required")
    LocalDateTime expiryDate;
}
