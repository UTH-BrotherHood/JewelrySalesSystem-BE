package com.example.JewelrySalesSystem.dto.request.SalesOrderDetailRequests;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
