package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderCreationRequest;
import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderResponse;
import com.example.JewelrySalesSystem.entity.SalesOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SalesOrderMapper {
    SalesOrder toSalesOrder(SalesOrderCreationRequest request);

    @Mapping(source = "returnPolicy", target = "returnPolicy")
    @Mapping(source = "originalTotalAmount", target = "originalTotalAmount")
    @Mapping(source = "discountedTotalAmount", target = "discountedTotalAmount")
    SalesOrderResponse toSalesOrderResponse(SalesOrder salesOrder);

    void updateSalesOrder(@MappingTarget SalesOrder salesOrder, SalesOrderUpdateRequest request);
}
