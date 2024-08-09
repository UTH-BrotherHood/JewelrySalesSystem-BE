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
    @NotNull(message = "CUSTOMER_ID_REQUIRED")
    String customerId;

    @NotNull(message = "EMPLOYEE_ID_REQUIRED")
    String employeeId;

    @NotNull(message = "ORDER_DATE_REQUIRED")
    LocalDateTime orderDate;

    @NotNull(message = "TOTAL_AMOUNT_REQUIRED")
    BigDecimal totalAmount;
}
