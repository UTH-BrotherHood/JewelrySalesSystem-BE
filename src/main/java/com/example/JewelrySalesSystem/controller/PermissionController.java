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
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        PermissionResponse response = permissionService.create(request);
        apiResponse.setCode(201);
        apiResponse.setMessage("Permission created successfully");
        apiResponse.setResult(response);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
        List<PermissionResponse> permissions = permissionService.getAll();
        apiResponse.setCode(200);
        apiResponse.setMessage("Permissions retrieved successfully");
        apiResponse.setResult(permissions);
        return apiResponse;
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<String> delete(@PathVariable String permission) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        permissionService.delete(permission);
        apiResponse.setCode(200);
        apiResponse.setMessage("Permission deleted successfully");
        apiResponse.setResult("Permission has been deleted");
        return apiResponse;
    }

}
