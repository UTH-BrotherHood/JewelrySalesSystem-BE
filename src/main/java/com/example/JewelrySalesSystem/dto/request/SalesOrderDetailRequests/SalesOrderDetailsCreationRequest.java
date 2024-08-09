package com.example.JewelrySalesSystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesOrderDetailsCreationRequest {
    String orderId;
    List<SalesOrderDetailCreationRequestz> products;
}
