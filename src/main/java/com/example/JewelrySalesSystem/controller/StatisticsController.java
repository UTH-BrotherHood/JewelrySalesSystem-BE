package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsCreationRequest;
import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.StatisticsResponse;
import com.example.JewelrySalesSystem.entity.SalesOrder;
import com.example.JewelrySalesSystem.service.StatisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping
    public ResponseEntity<ApiResponse<StatisticsResponse>> createStatistics(@Valid  @RequestBody StatisticsCreationRequest request) {
        StatisticsResponse response = statisticsService.createStatistics(request);
        ApiResponse<StatisticsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Statistics created successfully");
        apiResponse.setResult(response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{statisticId}")
    public ResponseEntity<ApiResponse<StatisticsResponse>> updateStatistics(
            @PathVariable String statisticId,
            @RequestBody StatisticsUpdateRequest request) {
        StatisticsResponse response = statisticsService.updateStatistics(statisticId, request);
        ApiResponse<StatisticsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Statistics updated successfully");
        apiResponse.setResult(response);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{statisticId}")
    public ResponseEntity<ApiResponse<Void>> deleteStatistics(@PathVariable String statisticId) {
        statisticsService.deleteStatistics(statisticId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(204);
        apiResponse.setMessage("Statistics deleted successfully");
        apiResponse.setResult(null);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{statisticId}")
    public ResponseEntity<ApiResponse<StatisticsResponse>> getStatistics(@PathVariable String statisticId) {
        StatisticsResponse response = statisticsService.getStatistics(statisticId);
        ApiResponse<StatisticsResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Statistics retrieved successfully");
        apiResponse.setResult(response);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StatisticsResponse>>> getStatistics(Pageable pageable) {
        Page<StatisticsResponse> statistics = statisticsService.getStatistics(pageable);
        ApiResponse<Page<StatisticsResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Statistics retrieved successfully");
        apiResponse.setResult(statistics);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/total-orders/date")
    public ResponseEntity<ApiResponse<Long>> getTotalOrdersByDate(@RequestParam LocalDateTime date) {
        Long totalOrders = statisticsService.getTotalOrdersByDate(date);
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Total orders by date retrieved successfully");
        apiResponse.setResult(totalOrders);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/total-orders/month")
    public ResponseEntity<ApiResponse<Long>> getTotalOrdersByMonth(@RequestParam int month, @RequestParam int year) {
        Long totalOrders = statisticsService.getTotalOrdersByMonth(month, year);
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Total orders by month retrieved successfully");
        apiResponse.setResult(totalOrders);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/total-sales/date")
    public ResponseEntity<ApiResponse<Double>> getTotalSalesAmountByDate(@RequestParam LocalDateTime date) {
        Double totalSales = statisticsService.getTotalSalesAmountByDate(date);
        ApiResponse<Double> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Total sales by date retrieved successfully");
        apiResponse.setResult(totalSales);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/total-sales/month")
    public ResponseEntity<ApiResponse<Double>> getTotalSalesAmountByMonth(@RequestParam int month, @RequestParam int year) {
        Double totalSales = statisticsService.getTotalSalesAmountByMonth(month, year);
        ApiResponse<Double> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Total sales by month retrieved successfully");
        apiResponse.setResult(totalSales);
        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/revenue-by-product")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByProduct(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Map<String, Object>> revenue = statisticsService.getRevenueByProduct(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }



    @GetMapping("/product-sales-quantities")
    public ResponseEntity<List<Map<String, Object>>> getProductSalesQuantities() {
        List<Map<String, Object>> quantities = statisticsService.getProductSalesQuantities();
        return ResponseEntity.ok(quantities);
    }

    @GetMapping("/product-stock-levels")
    public ResponseEntity<List<Map<String, Object>>> getProductStockLevels() {
        List<Map<String, Object>> stockLevels = statisticsService.getProductStockLevels();
        return ResponseEntity.ok(stockLevels);
    }

    @GetMapping("/top-selling-products")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingProducts() {
        List<Map<String, Object>> topProducts = statisticsService.getTopSellingProducts();
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/oldest-stock-products")
    public ResponseEntity<List<Map<String, Object>>> getOldestStockProducts() {
        List<Map<String, Object>> oldestProducts = statisticsService.getOldestStockProducts();
        return ResponseEntity.ok(oldestProducts);
    }

    @GetMapping("/revenue-by-customer")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByCustomer(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Map<String, Object>> revenue = statisticsService.getRevenueByCustomer(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }



    @GetMapping("/customer-purchase-history")
    public ResponseEntity<List<Map<String, Object>>> getCustomerPurchaseHistory() {
        List<Map<String, Object>> history = statisticsService.getCustomerPurchaseHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/revenue-by-employee")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByEmployee(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Map<String, Object>> revenue = statisticsService.getRevenueByEmployee(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

}
