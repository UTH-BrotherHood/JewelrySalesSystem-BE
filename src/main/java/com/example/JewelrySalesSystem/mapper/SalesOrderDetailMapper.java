package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.response.SalesOrderDetailResponse;
import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalesOrderDetailMapper {
    SalesOrderDetailResponse toSalesOrderDetailResponse(SalesOrderDetail salesOrderDetail);
}
