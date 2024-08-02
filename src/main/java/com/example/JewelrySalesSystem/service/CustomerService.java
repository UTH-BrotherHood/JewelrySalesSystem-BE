package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.CustomerCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CustomerUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CustomerResponse;
import com.example.JewelrySalesSystem.entity.Customer;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.CustomerMapper;
import com.example.JewelrySalesSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    public Customer createCustomer(CustomerCreationRequest request) {
        if (customerRepository.existsBycustomername(request.getCustomername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        Customer customer = customerMapper.toCustomer(request);

        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public CustomerResponse getCustomer(String id) {
        return customerMapper.toCustomerResponse(customerRepository.findById(id)
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
