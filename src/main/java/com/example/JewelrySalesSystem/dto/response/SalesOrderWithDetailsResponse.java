package com.example.JewelrySalesSystem.dto.response;

import com.example.JewelrySalesSystem.dto.response.SalesOrderResponse;
import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderWithDetailsResponse {
    private SalesOrderResponse salesOrder;
    private List<SalesOrderDetail> orderDetails;
}
