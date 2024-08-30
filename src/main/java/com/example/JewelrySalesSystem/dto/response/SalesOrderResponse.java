package com.example.JewelrySalesSystem.dto.response;

import com.example.JewelrySalesSystem.entity.Customer;
import com.example.JewelrySalesSystem.entity.Employee;
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

}