package com.example.JewelrySalesSystem.dto.request.SalesOrderRequests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesOrderCreationRequest {
    @NotNull(message = "Customer ID is required")
    String customerId;

    @NotNull(message = "Employee ID is required")
    String employeeId;


}

