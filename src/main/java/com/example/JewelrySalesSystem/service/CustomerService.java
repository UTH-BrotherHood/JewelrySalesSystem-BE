package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.constant.PredefinedRole;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CustomerResponse;
import com.example.JewelrySalesSystem.entity.Customer;
import com.example.JewelrySalesSystem.entity.Role;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.CustomerMapper;
import com.example.JewelrySalesSystem.repository.CustomerRepository;
import com.example.JewelrySalesSystem.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    RoleRepository roleRepository;

    public Customer createCustomer(CustomerCreationRequest request) {
        // Check if the customer name already exists
        if (customerRepository.existsBycustomername(request.getCustomername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Map the request to the Customer entity
        Customer customer = customerMapper.toCustomer(request);

        // Retrieve the CUSTOMER role
        Role customerRole = roleRepository.findByName(PredefinedRole.CUSTOMER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Create a Set<Role> and add the CUSTOMER role to it
        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);
        customer.setRoles(roles);

        // Save and return the customer
        return customerRepository.save(customer);
    }


    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public CustomerResponse getCustomer(String customerId) {
        return customerMapper.toCustomerResponse(customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public Customer updateCustomer(String customerId, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        customerMapper.updateCustomer(customer, request);
        return customerRepository.save(customer);
    }

    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        customerRepository.deleteById(customerId);
    }
}
