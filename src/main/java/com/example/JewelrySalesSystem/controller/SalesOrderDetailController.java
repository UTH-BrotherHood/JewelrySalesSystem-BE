package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.SalesOrderDetailRequests.SalesOrderDetailsCreationRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderDetailResponse;
import com.example.JewelrySalesSystem.service.SalesOrderDetailService;
import com.example.JewelrySalesSystem.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales-order-details")
@RequiredArgsConstructor
public class SalesOrderDetailController {
    private final SalesOrderDetailService salesOrderDetailService;
    private final SalesOrderService salesOrderService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<SalesOrderDetailResponse>>> getSalesOrderDetailsByOrderId(
            @PathVariable String orderId) {
        List<SalesOrderDetailResponse> salesOrderDetails = salesOrderDetailService.getSalesOrderDetailsByOrderId(orderId);
        ApiResponse<List<SalesOrderDetailResponse>> apiResponse = new ApiResponse<>(200, "Sales order details retrieved successfully", salesOrderDetails);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<SalesOrderDetailResponse>>> createSalesOrderDetails(
            @RequestBody SalesOrderDetailsCreationRequest request) {
        List<SalesOrderDetailResponse> salesOrderDetailsResponses = salesOrderDetailService.createSalesOrderDetails(request);
        ApiResponse<List<SalesOrderDetailResponse>> apiResponse = new ApiResponse<>(201, "Sales order details created successfully", salesOrderDetailsResponses);
        return ResponseEntity.status(201).body(apiResponse);
    }


}
