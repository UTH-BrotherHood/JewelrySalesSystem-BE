package com.example.JewelrySalesSystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesOrderUpdateRequest {
    String customerId;
    String employeeId;
    LocalDateTime orderDate;
    BigDecimal totalAmount;
}