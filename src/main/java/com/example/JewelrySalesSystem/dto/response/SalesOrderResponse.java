package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesOrderResponse {
    String orderId;
    String customerId;
    String employeeId;
    LocalDateTime orderDate;
    BigDecimal totalAmount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}