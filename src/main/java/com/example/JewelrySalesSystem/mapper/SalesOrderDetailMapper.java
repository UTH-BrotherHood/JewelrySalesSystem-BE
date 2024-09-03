package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.response.SalesOrderDetailResponse;
import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalesOrderDetailMapper {
    @Mapping(target = "priceAfterDiscount", source = "priceAfterDiscount")
    SalesOrderDetailResponse toSalesOrderDetailResponse(SalesOrderDetail salesOrderDetail);
}
