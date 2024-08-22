package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.RoleRequests.RoleRequest;
import com.example.JewelrySalesSystem.dto.response.RoleResponse;
import com.example.JewelrySalesSystem.service.RoleService;
import jakarta.validation.Valid;
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
    public ApiResponse<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        RoleResponse response = roleService.create(request);
        apiResponse.setCode(201);
        apiResponse.setMessage("Role created successfully");
        apiResponse.setResult(response);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        List<RoleResponse> roles = roleService.getAll();
        apiResponse.setCode(200);
        apiResponse.setMessage("Roles retrieved successfully");
        apiResponse.setResult(roles);
        return apiResponse;
    }

    @DeleteMapping("/{role}")
    public ApiResponse<Void> delete(@PathVariable String role) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        roleService.delete(role);
        apiResponse.setCode(204);
        apiResponse.setMessage("Role deleted successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }

}
