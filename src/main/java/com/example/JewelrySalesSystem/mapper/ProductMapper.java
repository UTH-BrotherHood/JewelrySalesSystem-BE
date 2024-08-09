package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductCreationRequest;
import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.ProductResponse;
import com.example.JewelrySalesSystem.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface ProductMapper {
    Product toProduct(ProductCreationRequest request);

    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);

    ProductResponse toProductResponse(Product product);
}
