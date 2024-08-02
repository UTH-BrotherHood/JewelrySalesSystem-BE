package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.EmployeeCreationRequest;
import com.example.JewelrySalesSystem.dto.response.EmployeeResponse;
import com.example.JewelrySalesSystem.entity.Employee;
import com.example.JewelrySalesSystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ApiResponse<Employee> createEmployee(@RequestBody @Valid EmployeeCreationRequest request) {
        ApiResponse<Employee> apiResponse = new ApiResponse<>();
        Employee createEmployee = employeeService.createEmployee(request);
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

    @GetMapping("/{employeeId}")
    public ApiResponse<EmployeeResponse> getEmployee(@PathVariable("employeeId") Integer employeeId) {
        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();
        EmployeeResponse employee = employeeService.getEmployee(employeeId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Employee retrieved successfully");
        apiResponse.setResult(employee);
        return apiResponse;
    }

    @PutMapping("/{employeeId}")
    public ApiResponse<Employee> updateEmployee(@PathVariable Integer employeeId, @RequestBody @Valid EmployeeCreationRequest request) {
        ApiResponse<Employee> apiResponse = new ApiResponse<>();
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, request);
        apiResponse.setCode(200);
        apiResponse.setMessage("Employee updated successfully");
        apiResponse.setResult(updatedEmployee);
        return apiResponse;
    }

    @DeleteMapping("/{employeeId}")
    public ApiResponse<String> deleteEmployee(@PathVariable Integer employeeId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        employeeService.deleteEmployee(employeeId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Employee has been deleted");
        apiResponse.setResult("Employee has been deleted");
        return apiResponse;
    }
}
