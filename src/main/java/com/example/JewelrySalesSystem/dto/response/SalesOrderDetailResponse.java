package com.example.JewelrySalesSystem.dto.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesOrderDetailResponse {
    String orderDetailId;
    String orderId;
    String productId;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice; // Giá tổng trước khi giảm giá
    BigDecimal priceAfterDiscount; // Giá tổng sau khi giảm giá
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
