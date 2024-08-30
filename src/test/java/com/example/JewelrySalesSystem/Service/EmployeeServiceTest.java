package com.example.JewelrySalesSystem.Service;

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
import com.example.JewelrySalesSystem.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private EmployeeMapper employeeMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private EmployeeService employeeService;

  private Employee employee;
  private Role role;

  @BeforeEach
  void setUp() {
    employee = new Employee();
    employee.setUsername("user");
    employee.setPassword("password");

    role = new Role();
    role.setName("EMPLOYEE_ROLE");
  }

  @Test
  void testCreateEmployee_Success() {
    EmployeeCreationRequest request = new EmployeeCreationRequest();
    request.setUsername("user");
    request.setPassword("password");
    request.setPhoneNumber("123456789");

    when(employeeRepository.existsByUsername("user")).thenReturn(false);
    when(employeeMapper.toEmployee(request)).thenReturn(employee);
    when(passwordEncoder.encode("password")).thenReturn("encoded_password");
    when(roleRepository.findByName("EMPLOYEE_ROLE")).thenReturn(Optional.of(role));
    when(employeeRepository.save(employee)).thenReturn(employee);

    Employee createdEmployee = employeeService.createEmployee(request);

    assertNotNull(createdEmployee);
    assertEquals("encoded_password", createdEmployee.getPassword());
    verify(employeeRepository, times(1)).save(employee);
  }

  @Test
  void testCreateEmployee_UserExists() {
    EmployeeCreationRequest request = new EmployeeCreationRequest();
    request.setUsername("user");

    when(employeeRepository.existsByUsername("user")).thenReturn(true);

    AppException exception = assertThrows(AppException.class, () -> employeeService.createEmployee(request));
    assertEquals(ErrorCode.USER_EXISTED, exception.getErrorCode());
    verify(employeeRepository, never()).save(any(Employee.class));
  }

  @Test
  void testGetEmployees() {
    when(employeeRepository.findAll()).thenReturn(List.of(employee));

    List<Employee> employees = employeeService.getEmployees();

    assertNotNull(employees);
    assertEquals(1, employees.size());
  }

  @Test
  void testGetEmployee_Success() {
    when(employeeRepository.findById("1")).thenReturn(Optional.of(employee));
    EmployeeResponse expectedResponse = new EmployeeResponse();
    expectedResponse.setUsername("user");
    when(employeeMapper.toEmployeeResponse(employee)).thenReturn(expectedResponse);

    EmployeeResponse response = employeeService.getEmployee("1");

    assertNotNull(response);
    assertEquals("user", response.getUsername());
  }

  @Test
  void testGetEmployee_UserNotFound() {
    when(employeeRepository.findById("1")).thenReturn(Optional.empty());

    AppException exception = assertThrows(AppException.class, () -> employeeService.getEmployee("1"));
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void testUpdateEmployee_Success() {
    EmployeeUpdateRequest request = new EmployeeUpdateRequest();
    request.setUsername("newUser");

    when(employeeRepository.findById("1")).thenReturn(Optional.of(employee));
    when(roleRepository.findAllById(request.getRoles())).thenReturn(List.of(role));
    when(employeeRepository.save(employee)).thenReturn(employee);

    EmployeeResponse expectedResponse = new EmployeeResponse();
    expectedResponse.setUsername("newUser");
    when(employeeMapper.toEmployeeResponse(employee)).thenReturn(expectedResponse);

    EmployeeResponse response = employeeService.updateEmployee("1", request);

    assertNotNull(response);
    assertEquals("newUser", response.getUsername());
  }

  @Test
  void testUpdateEmployee_UserNotFound() {
    EmployeeUpdateRequest request = new EmployeeUpdateRequest();

    when(employeeRepository.findById("1")).thenReturn(Optional.empty());

    AppException exception = assertThrows(AppException.class, () -> employeeService.updateEmployee("1", request));
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void testDeleteEmployee_Success() {
    when(employeeRepository.existsById("1")).thenReturn(true);

    employeeService.deleteEmployee("1");

    verify(employeeRepository, times(1)).deleteById("1");
  }

  @Test
  void testDeleteEmployee_UserNotFound() {
    when(employeeRepository.existsById("1")).thenReturn(false);

    AppException exception = assertThrows(AppException.class, () -> employeeService.deleteEmployee("1"));
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }
}
