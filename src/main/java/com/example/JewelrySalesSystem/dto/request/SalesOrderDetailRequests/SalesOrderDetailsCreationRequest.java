package com.example.JewelrySalesSystem.dto.request.SalesOrderDetailRequests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesOrderDetailsCreationRequest {
    @NotNull(message = "Order ID is required")
    String orderId;
    @NotNull(message = "Products is required")
    List<SalesOrderDetailCreationRequestz> products;
}
