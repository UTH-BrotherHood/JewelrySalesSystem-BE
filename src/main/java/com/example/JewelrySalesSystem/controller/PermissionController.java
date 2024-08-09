package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.PermissionRequests.PermissionRequest;
import com.example.JewelrySalesSystem.dto.response.PermissionResponse;
import com.example.JewelrySalesSystem.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> create(@RequestBody PermissionRequest request) {
        PermissionResponse response = permissionService.create(request);
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>(201, "Permission created successfully", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAll() {
        List<PermissionResponse> permissions = permissionService.getAll();
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>(200, "Permissions retrieved successfully", permissions);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{permission}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String permission) {
        permissionService.delete(permission);
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Permission deleted successfully", null);
        return ResponseEntity.noContent().build();
    }
}
