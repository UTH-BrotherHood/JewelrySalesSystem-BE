package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.EmployeeCreationRequest;
import com.example.JewelrySalesSystem.dto.response.EmployeeResponse;
import com.example.JewelrySalesSystem.entity.Employee;
import com.example.JewelrySalesSystem.enums.Role;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.EmployeeMapper;
import com.example.JewelrySalesSystem.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;
    private PasswordEncoder passwordEncoder;

    public Employee createEmployee(EmployeeCreationRequest request) {
        if (employeeRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        Employee employee = employeeMapper.toEmployee(request);

        employee.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.EMPLOYEE.name());
//        employee.setRoles(roles);

        employee.setPhoneNumber(request.getPhoneNumber());

        return employeeRepository.save(employee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public EmployeeResponse getEmployee(String employeeId) {
        return employeeMapper.toEmployeeResponse(employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public EmployeeResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Employee employee = employeeRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return employeeMapper.toEmployeeResponse(employee);
    }

    public Employee updateEmployee(String employeeId, EmployeeCreationRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        employee.setName(request.getName());
        employee.setUsername(request.getUsername());

        employee.setPassword(new BCryptPasswordEncoder(10).encode(request.getPassword()));
        employee.setPhoneNumber(request.getPhoneNumber());
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(String employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        employeeRepository.deleteById(employeeId);
    }
}
