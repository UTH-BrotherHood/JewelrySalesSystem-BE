package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.RoleRequests.RoleRequest;
import com.example.JewelrySalesSystem.dto.response.RoleResponse;
import com.example.JewelrySalesSystem.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody RoleRequest request) {
        RoleResponse response = roleService.create(request);
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>(201, "Role created successfully", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
        List<RoleResponse> roles = roleService.getAll();
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>(200, "Roles retrieved successfully", roles);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{role}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String role) {
        roleService.delete(role);
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Role deleted successfully", null);
        return ResponseEntity.noContent().build();
    }
}
