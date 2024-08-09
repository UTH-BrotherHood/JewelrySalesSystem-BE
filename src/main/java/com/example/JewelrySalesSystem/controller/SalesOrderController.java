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
    public ResponseEntity<ApiResponse<Page<SalesOrderResponse>>> getSalesOrdersByEmployeeId(
            @PathVariable String employeeId, Pageable pageable) {
        Page<SalesOrderResponse> salesOrders = salesOrderService.getSalesOrdersByEmployeeId(employeeId, pageable);
        ApiResponse<Page<SalesOrderResponse>> apiResponse = new ApiResponse<>(200, "Sales orders retrieved successfully", salesOrders);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<Page<SalesOrderResponse>>> getSalesOrdersByCustomerId(
            @PathVariable String customerId, Pageable pageable) {
        Page<SalesOrderResponse> salesOrders = salesOrderService.getSalesOrdersByCustomerId(customerId, pageable);
        ApiResponse<Page<SalesOrderResponse>> apiResponse = new ApiResponse<>(200, "Sales orders retrieved successfully", salesOrders);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SalesOrderResponse>> createSalesOrder(@RequestBody SalesOrderCreationRequest request) {
        SalesOrderResponse salesOrderResponse = salesOrderService.createSalesOrder(request);
        ApiResponse<SalesOrderResponse> apiResponse = new ApiResponse<>(201, "Sales order created successfully", salesOrderResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> updateSalesOrder(
            @PathVariable String orderId,
            @RequestBody SalesOrderUpdateRequest request) {
        SalesOrderResponse salesOrderResponse = salesOrderService.updateSalesOrder(orderId, request);
        ApiResponse<SalesOrderResponse> apiResponse = new ApiResponse<>(200, "Sales order updated successfully", salesOrderResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteSalesOrder(@PathVariable String orderId) {
        salesOrderService.deleteSalesOrder(orderId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Sales order deleted successfully", null);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> getSalesOrder(@PathVariable String orderId) {
        SalesOrderResponse salesOrderResponse = salesOrderService.getSalesOrder(orderId);
        ApiResponse<SalesOrderResponse> apiResponse = new ApiResponse<>(200, "Sales order retrieved successfully", salesOrderResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SalesOrderResponse>>> getSalesOrders(Pageable pageable) {
        Page<SalesOrderResponse> salesOrders = salesOrderService.getSalesOrders(pageable);
        ApiResponse<Page<SalesOrderResponse>> apiResponse = new ApiResponse<>(200, "Sales orders retrieved successfully", salesOrders);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<ApiResponse<SalesOrderWithDetailsResponse>> getSalesOrderWithDetails(@PathVariable String orderId) {
        SalesOrderWithDetailsResponse salesOrderWithDetailsResponse = salesOrderService.getSalesOrderWithDetails(orderId);
        ApiResponse<SalesOrderWithDetailsResponse> apiResponse = new ApiResponse<>(200, "Sales order with details retrieved successfully", salesOrderWithDetailsResponse);
        return ResponseEntity.ok(apiResponse);
    }
}
