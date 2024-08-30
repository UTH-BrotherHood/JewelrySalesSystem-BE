package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.constant.PredefinedRole;
import com.example.JewelrySalesSystem.dto.request.EmployeeRequests.EmployeeCreationRequest;
import com.example.JewelrySalesSystem.dto.request.EmployeeRequests.EmployeeUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.EmployeeResponse;
import com.example.JewelrySalesSystem.entity.Employee;
import com.example.JewelrySalesSystem.entity.Role;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.EmployeeMapper;
import com.example.JewelrySalesSystem.repository.EmployeeRepository;
import com.example.JewelrySalesSystem.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class EmployeeService {
    EmployeeRepository employeeRepository;
    EmployeeMapper employeeMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public EmployeeResponse createEmployee(EmployeeCreationRequest request) {
        if (employeeRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Employee employee = employeeMapper.toEmployee(request);
        employee.setPassword(passwordEncoder.encode(request.getPassword()));

        Role employeeRole = roleRepository.findByName(PredefinedRole.EMPLOYEE_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        var roles = new HashSet<Role>();
        roles.add(employeeRole);
        employee.setRoles(roles);

        employee.setPhoneNumber(request.getPhoneNumber());

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toEmployeeResponse(savedEmployee);
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

    @PreAuthorize("hasAuthority('REJECT_POST')")
    public EmployeeResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Employee employee = employeeRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return employeeMapper.toEmployeeResponse(employee);
    }

    public EmployeeResponse updateEmployee(String employeeId, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        employeeMapper.updateEmployee(employee, request);

        var roles = roleRepository.findAllById(request.getRoles());
        employee.setRoles(new HashSet<>(roles));
        return employeeMapper.toEmployeeResponse(employeeRepository.save(employee));
    }

    public void deleteEmployee(String employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        employeeRepository.deleteById(employeeId);
    }
}
