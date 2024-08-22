package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsCreationRequest;
import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.StatisticsResponse;
import com.example.JewelrySalesSystem.service.StatisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
