package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.WarrantyRequests.WarrantyCreationRequest;
import com.example.JewelrySalesSystem.dto.response.WarrantyResponse;
import com.example.JewelrySalesSystem.service.WarrantyService;
import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warranties")
@RequiredArgsConstructor
public class WarrantyController {

    private final WarrantyService warrantyService;

    @PostMapping
    public ApiResponse<WarrantyResponse> createWarranty(@RequestBody @Valid WarrantyCreationRequest request) {
        return ApiResponse.<WarrantyResponse>builder()
                .result(warrantyService.createWarranty(request))
                .message("Warranty created successfully")
                .code(201)
                .build();
    }

    @GetMapping("/{warrantyId}")
    public ApiResponse<WarrantyResponse> getWarranty(@PathVariable String warrantyId) {
        return ApiResponse.<WarrantyResponse>builder()
                .result(warrantyService.getWarranty(warrantyId))
                .message("Warranty retrieved successfully")
                .code(200)
                .build();
    }

    @DeleteMapping("/{warrantyId}")
    public ApiResponse<String> deleteWarranty(@PathVariable String warrantyId) {
        warrantyService.deleteWarranty(warrantyId);
        return ApiResponse.<String>builder()
                .result("Warranty deleted successfully")
                .message("Warranty deleted successfully")
                .code(200)
                .build();
    }

    @GetMapping("/product/{productId}")
    public ApiResponse<List<WarrantyResponse>> getWarrantiesByProductId(@PathVariable String productId) {
        return ApiResponse.<List<WarrantyResponse>>builder()
                .result(warrantyService.getWarrantiesByProductId(productId))
                .message("Warranties retrieved successfully")
                .code(200)
                .build();
    }

    @GetMapping
    public ApiResponse<List<WarrantyResponse>> getAllWarranties() {
        return ApiResponse.<List<WarrantyResponse>>builder()
                .result(warrantyService.getAllWarranties())
                .message("All warranties retrieved successfully")
                .code(200)
                .build();
    }
}
