package com.example.JewelrySalesSystem.mapper;


import com.example.JewelrySalesSystem.dto.request.WarrantyRequests.WarrantyCreationRequest;
import com.example.JewelrySalesSystem.dto.response.WarrantyResponse;
import com.example.JewelrySalesSystem.entity.Warranty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface WarrantyMapper {
    @Mapping(source = "productId", target = "product.productId")
    Warranty toWarranty(WarrantyCreationRequest request);

    @Mapping(source = "product.productId", target = "productId")
    WarrantyResponse toWarrantyResponse(Warranty warranty);
}
