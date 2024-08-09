package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CustomerResponse;
import com.example.JewelrySalesSystem.entity.Customer;
import com.example.JewelrySalesSystem.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;


    @PostMapping
    public ApiResponse<Customer> createCustomer(@RequestBody @Valid CustomerCreationRequest request) {
        ApiResponse<Customer> apiResponse = new ApiResponse<>();
        Customer createCustomer = customerService.createCustomer(request);
        apiResponse.setCode(201);
        apiResponse.setMessage("Customer created successfully");
        apiResponse.setResult(createCustomer);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<Customer>> getCustomers() {
        ApiResponse<List<Customer>> apiResponse = new ApiResponse<>();
        List<Customer> customers = customerService.getCustomers();
        apiResponse.setCode(200);
        apiResponse.setMessage("Customers retrieved successfully");
        apiResponse.setResult(customers);
        return apiResponse;
    }

    @GetMapping("/{customerId}")
    public ApiResponse<CustomerResponse> getCustomer(@PathVariable("customerId") String customerId) {
        ApiResponse<CustomerResponse> apiResponse = new ApiResponse<>();
        CustomerResponse customer = customerService.getCustomer(customerId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Customer retrieved successfully");
        apiResponse.setResult(customer);
        return apiResponse;
    }

    @PutMapping("/{customerId}")
    public ApiResponse<Customer> updateCustomer(@PathVariable String customerId, @RequestBody @Valid CustomerUpdateRequest request) {
        ApiResponse<Customer> apiResponse = new ApiResponse<>();
        Customer updatedCustomer = customerService.updateCustomer(customerId, request);
        apiResponse.setCode(200);
        apiResponse.setMessage("Customer updated successfully");
        apiResponse.setResult(updatedCustomer);
        return apiResponse;
    }

    @DeleteMapping("/{customerId}")
    public ApiResponse<String> deleteCustomer(@PathVariable String customerId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        customerService.deleteCustomer(customerId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Customer has been deleted");
        apiResponse.setResult("Customer has been deleted");
        return apiResponse;
    }




}
