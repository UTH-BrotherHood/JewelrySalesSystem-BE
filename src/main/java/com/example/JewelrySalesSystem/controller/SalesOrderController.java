package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderCreationRequest;
import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderResponse;
import com.example.JewelrySalesSystem.dto.response.SalesOrderWithDetailsResponse;
import com.example.JewelrySalesSystem.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {
    private final SalesOrderService salesOrderService;

    @GetMapping("/employee/{employeeId}")
    public ApiResponse<Page<SalesOrderResponse>> getSalesOrdersByEmployeeId(
            @PathVariable String employeeId, Pageable pageable) {
        Page<SalesOrderResponse> salesOrders = salesOrderService.getSalesOrdersByEmployeeId(employeeId, pageable);
        ApiResponse<Page<SalesOrderResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Sales orders retrieved successfully");
        apiResponse.setResult(salesOrders);
        return apiResponse;
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<Page<SalesOrderResponse>> getSalesOrdersByCustomerId(
            @PathVariable String customerId, Pageable pageable) {
        Page<SalesOrderResponse> salesOrders = salesOrderService.getSalesOrdersByCustomerId(customerId, pageable);
        ApiResponse<Page<SalesOrderResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Sales orders retrieved successfully");
        apiResponse.setResult(salesOrders);
        return apiResponse;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SalesOrderResponse>> createSalesOrder(@RequestBody SalesOrderCreationRequest request) {
        SalesOrderResponse salesOrderResponse = salesOrderService.createSalesOrder(request);
        ApiResponse<SalesOrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Sales order created successfully");
        apiResponse.setResult(salesOrderResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ApiResponse<SalesOrderResponse> updateSalesOrder(
            @PathVariable String orderId,
            @RequestBody SalesOrderUpdateRequest request) {
        SalesOrderResponse salesOrderResponse = salesOrderService.updateSalesOrder(orderId, request);
        ApiResponse<SalesOrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Sales order updated successfully");
        apiResponse.setResult(salesOrderResponse);
        return apiResponse;
    }

    @DeleteMapping("/{orderId}")
    public ApiResponse<Void> deleteSalesOrder(@PathVariable String orderId) {
        salesOrderService.deleteSalesOrder(orderId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(204);
        apiResponse.setMessage("Sales order deleted successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }

    @GetMapping("/{orderId}")
    public ApiResponse<SalesOrderResponse> getSalesOrder(@PathVariable String orderId) {
        SalesOrderResponse salesOrderResponse = salesOrderService.getSalesOrder(orderId);
        ApiResponse<SalesOrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Sales order retrieved successfully");
        apiResponse.setResult(salesOrderResponse);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<Page<SalesOrderResponse>> getSalesOrders(Pageable pageable) {
        Page<SalesOrderResponse> salesOrders = salesOrderService.getSalesOrders(pageable);
        ApiResponse<Page<SalesOrderResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Sales orders retrieved successfully");
        apiResponse.setResult(salesOrders);
        return apiResponse;
    }

    @GetMapping("/{orderId}/details")
    public ApiResponse<SalesOrderWithDetailsResponse> getSalesOrderWithDetails(@PathVariable String orderId) {
        SalesOrderWithDetailsResponse salesOrderWithDetailsResponse = salesOrderService.getSalesOrderWithDetails(orderId);
        ApiResponse<SalesOrderWithDetailsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Sales order with details retrieved successfully");
        apiResponse.setResult(salesOrderWithDetailsResponse);
        return apiResponse;
    }
}
