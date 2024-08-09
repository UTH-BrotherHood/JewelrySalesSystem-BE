package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CustomerResponse;
import com.example.JewelrySalesSystem.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface CustomerMapper {
    Customer toCustomer(CustomerCreationRequest request);

    CustomerResponse toCustomerResponse(Customer customer);

    void updateCustomer(@MappingTarget Customer customer, CustomerUpdateRequest request);
}


