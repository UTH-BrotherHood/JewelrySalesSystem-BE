package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.SalesOrderDetailRequests.SalesOrderDetailsCreationRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderDetailResponse;
import com.example.JewelrySalesSystem.service.SalesOrderDetailService;
import com.example.JewelrySalesSystem.service.SalesOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales-order-details")
@RequiredArgsConstructor
public class SalesOrderDetailController {
    private final SalesOrderDetailService salesOrderDetailService;
    private final SalesOrderService salesOrderService;

//    @GetMapping("/order/{orderId}")
//    public ResponseEntity<ApiResponse<List<SalesOrderDetailResponse>>> getSalesOrderDetailsByOrderId(
//            @PathVariable String orderId) {
//        List<SalesOrderDetailResponse> salesOrderDetails = salesOrderDetailService.getSalesOrderDetailsByOrderId(orderId);
//        ApiResponse<List<SalesOrderDetailResponse>> apiResponse = new ApiResponse<>();
//        apiResponse.setCode(200);
//        apiResponse.setMessage("Sales order details retrieved successfully");
//        apiResponse.setResult(salesOrderDetails);
//        return ResponseEntity.ok(apiResponse);
//    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<SalesOrderDetailResponse>>> createSalesOrderDetails(
          @Valid @RequestBody SalesOrderDetailsCreationRequest request) {
        List<SalesOrderDetailResponse> salesOrderDetailsResponses = salesOrderDetailService.createSalesOrderDetails(request);
        ApiResponse<List<SalesOrderDetailResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Sales order details created successfully");
        apiResponse.setResult(salesOrderDetailsResponses);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
