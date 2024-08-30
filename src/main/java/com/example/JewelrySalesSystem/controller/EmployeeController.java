package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.EmployeeRequests.EmployeeCreationRequest;
import com.example.JewelrySalesSystem.dto.request.EmployeeRequests.EmployeeUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.EmployeeResponse;
import com.example.JewelrySalesSystem.entity.Employee;
import com.example.JewelrySalesSystem.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ApiResponse<EmployeeResponse> createEmployee(@RequestBody @Valid EmployeeCreationRequest request) {
        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();
        EmployeeResponse createEmployee = employeeService.createEmployee(request);
        apiResponse.setCode(201);
        apiResponse.setMessage("Employee created successfully");
        apiResponse.setResult(createEmployee);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<Employee>> getEmployees() {
        ApiResponse<List<Employee>> apiResponse = new ApiResponse<>();
        List<Employee> employees = employeeService.getEmployees();
        apiResponse.setCode(200);
        apiResponse.setMessage("Employees retrieved successfully");
        apiResponse.setResult(employees);
        return apiResponse;
    }

    @GetMapping("/myInfo")
    public ApiResponse<EmployeeResponse> getMyInfo(){
        return ApiResponse.<EmployeeResponse>builder()
                .result(employeeService.getMyInfo())
                .build();
    }

    @GetMapping("/{employeeId}")
    public ApiResponse<EmployeeResponse> getEmployee(@PathVariable("employeeId") String employeeId) {
        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();
        EmployeeResponse employee = employeeService.getEmployee(employeeId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Employee retrieved successfully");
        apiResponse.setResult(employee);
        return apiResponse;
    }

    @PutMapping("/{employeeId}")
    public ApiResponse<EmployeeResponse> updateUser(@PathVariable String employeeId, @RequestBody EmployeeUpdateRequest request){
        return ApiResponse.<EmployeeResponse>builder()
                .result(employeeService.updateEmployee(employeeId, request))
                .build();
    }

    @DeleteMapping("/{employeeId}")
    public ApiResponse<String> deleteEmployee(@PathVariable String employeeId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        employeeService.deleteEmployee(employeeId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Employee has been deleted");
        apiResponse.setResult("Employee has been deleted");
        return apiResponse;
    }
}
