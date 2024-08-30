package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderCreationRequest;
import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderResponse;
import com.example.JewelrySalesSystem.dto.response.SalesOrderWithDetailsResponse;
import com.example.JewelrySalesSystem.entity.SalesOrder;
import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalesOrderMapper {
    SalesOrder toSalesOrder(SalesOrderCreationRequest request);

    SalesOrderResponse toSalesOrderResponse(SalesOrder salesOrder);

    void updateSalesOrder(@MappingTarget SalesOrder salesOrder, SalesOrderUpdateRequest request);

    SalesOrderWithDetailsResponse toSalesOrderWithDetailsResponse(SalesOrder salesOrder, List<SalesOrderDetail> orderDetails);
}
