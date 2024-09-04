package com.example.JewelrySalesSystem.controller;


import com.example.JewelrySalesSystem.constant.PredefinedRole;
import com.example.JewelrySalesSystem.dto.request.EmployeeRequests.EmployeeCreationRequest;
import com.example.JewelrySalesSystem.dto.request.EmployeeRequests.EmployeeUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.EmployeeResponse;
import com.example.JewelrySalesSystem.dto.response.PermissionResponse;
import com.example.JewelrySalesSystem.dto.response.RoleResponse;
import com.example.JewelrySalesSystem.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource("/test.properties")
@WithMockUser(username = "testUser", roles = {"ADMIN"})
public class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    // tạo trước 2 lớp request và response
    EmployeeCreationRequest employeeCreationRequest;
    EmployeeResponse employeeResponse;
    ObjectMapper objectMapper;
    LocalDateTime date;
    final String employeeId = "employee_id_test";

    @BeforeEach
    void initData() {
        date = LocalDateTime.of(2024, Month.SEPTEMBER, 4, 0, 0, 0);
        objectMapper = new ObjectMapper();

        employeeCreationRequest = EmployeeCreationRequest.builder()
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .phoneNumber("0123456789")
                .build();

        employeeResponse = EmployeeResponse.builder()
                .employeeId(employeeId)
                .name("John Doe")
                .username("johndoe")
                .createdAt(date)
                .updatedAt(date)
                .roles(Set.of(RoleResponse.builder()
                        .name(PredefinedRole.EMPLOYEE_ROLE)
                        .description("Employee role")
                        .permissions(Set.of(PermissionResponse.builder()
                                .name("")
                                .description("")
                                .build()))
                        .build()))
                .build();

    }

    @Test
    void createEmployee_validRequest_success() throws Exception {
        objectMapper.registerModule(new JavaTimeModule()); // Đăng ký module để mapper có thể serialize được LocalDateTime
        String content = objectMapper.writeValueAsString(employeeCreationRequest);

        // Giả lập kết quả cho sự kiện createCategory
        when(employeeService.createEmployee(any())).thenReturn(employeeResponse);

        // Giả lập request, ko check ngày vì sợ sau này có thể thay đổi format, chủ yếu mình check những dữ liệu khác như statuscode, content.
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.employeeId").value(employeeResponse.getEmployeeId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(employeeResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(employeeResponse.getUsername()));
    }

    @Test
    void createEmployees_errorValidator_badRequest() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        employeeCreationRequest.setName("");
        String invalidContent = objectMapper.writeValueAsString(employeeCreationRequest);

        // Giả lập request nếu ko có name
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(invalidContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("Name must be at least 4 characters long"));
    }

    @Test
    void getEmployees_validRequest_success() throws Exception {
        when(employeeService.getEmployees()).thenReturn(Collections.singletonList(employeeResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employees retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].employeeId").value(employeeResponse.getEmployeeId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].name").value(employeeResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].username").value(employeeResponse.getUsername()));

    }

    @Test
    void getEmployee_validRequest_success() throws Exception {
        when(employeeService.getEmployee(employeeId)).thenReturn(employeeResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.employeeId").value(employeeResponse.getEmployeeId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(employeeResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(employeeResponse.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].name").value(PredefinedRole.EMPLOYEE_ROLE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].description").value("Employee role"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].permissions[0].name").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].permissions[0].description").value(""));
    }

    @Test
    void updateEmployee_validRequest_success() throws Exception {
        EmployeeUpdateRequest employeeUpdateRequest = EmployeeUpdateRequest.builder()
                .name("John Doe Updated")
                .build();

        String content = objectMapper.writeValueAsString(employeeUpdateRequest);

        EmployeeResponse updatedEmployeeResponse = EmployeeResponse.builder()
                .employeeId(employeeId)
                .name("John Doe Updated")
                .username("johndoe")
                .createdAt(date)
                .updatedAt(date)
                .roles(Set.of(RoleResponse.builder()
                        .name(PredefinedRole.EMPLOYEE_ROLE)
                        .description("Employee role")
                        .permissions(Set.of(PermissionResponse.builder()
                                .name("")
                                .description("")
                                .build()))
                        .build()))
                .build();

        when(employeeService.updateEmployee(any(String.class), any(EmployeeUpdateRequest.class)))
                .thenReturn(updatedEmployeeResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.employeeId").value(updatedEmployeeResponse.getEmployeeId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(updatedEmployeeResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(updatedEmployeeResponse.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].description").value("Employee role"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].permissions[0].name").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].permissions[0].description").value(""));


    }

    @Test
    void deleteEmployee_validRequest_success() throws Exception {
        doNothing().when(employeeService).deleteEmployee(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{employeeId}", employeeId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee has been deleted"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Employee has been deleted"));

        verify(employeeService).deleteEmployee(employeeId);
    }


}
