package com.example.JewelrySalesSystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethodCreationRequest {
     @NotNull(message = "paymentMethodName is required")
     String paymentMethodName;
     @NotNull(message = "details is required")
     String details;

     boolean active;
}
