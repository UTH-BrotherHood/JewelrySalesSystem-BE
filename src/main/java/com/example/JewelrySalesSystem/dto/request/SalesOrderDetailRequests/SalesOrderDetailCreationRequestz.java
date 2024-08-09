package com.example.JewelrySalesSystem.dto.request.SalesOrderDetailRequests;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesOrderDetailCreationRequestz {
    String productId;
    int quantity;
    BigDecimal unitPrice;
}