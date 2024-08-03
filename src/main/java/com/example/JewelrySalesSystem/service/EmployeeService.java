package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.EmployeeCreationRequest;
import com.example.JewelrySalesSystem.dto.response.EmployeeResponse;
import com.example.JewelrySalesSystem.entity.Employee;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.EmployeeMapper;
import com.example.JewelrySalesSystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeMapper employeeMapper;

    public Employee createEmployee(EmployeeCreationRequest request) {
        if (employeeRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        Employee employee = employeeMapper.toEmployee(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole(request.getRole());
        employee.setPhoneNumber(request.getPhoneNumber());

        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeResponse getEmployee(Integer employeeId) {
        return employeeMapper.toEmployeeResponse(employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public Employee updateEmployee(Integer employeeId, EmployeeCreationRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        employee.setName(request.getName());
        employee.setUsername(request.getUsername());
        employee.setRole(request.getRole());
        employee.setPassword(new BCryptPasswordEncoder(10).encode(request.getPassword()));
        employee.setPhoneNumber(request.getPhoneNumber()); // Cập nhật phoneNumber
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Integer employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        employeeRepository.deleteById(employeeId);
    }
}
