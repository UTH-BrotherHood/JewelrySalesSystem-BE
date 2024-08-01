package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.CustomerCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CustomerUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CustomerResponse;
import com.example.JewelrySalesSystem.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserMapper {
    Customer toUser(CustomerCreationRequest request);

    CustomerResponse toUserResponse(Customer customer);

    void updateUser(@MappingTarget Customer customer, CustomerUpdateRequest request);
}
