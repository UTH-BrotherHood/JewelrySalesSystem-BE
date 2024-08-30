package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.constant.PredefinedRole;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CustomerResponse;
import com.example.JewelrySalesSystem.dto.response.PermissionResponse;
import com.example.JewelrySalesSystem.dto.response.RoleResponse;
import com.example.JewelrySalesSystem.service.CustomerService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource("/test.properties")
@WithMockUser(username = "testUser", roles = {"ADMIN"})
class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    CustomerResponse customerResponse;
    ObjectMapper objectMapper;
    final String customerId = "customer_id_test";
    LocalDateTime date;

    @BeforeEach
    void initData() {
        objectMapper = new ObjectMapper();
        date = LocalDateTime.of(2024, Month.AUGUST, 30, 0, 0, 0);

        customerResponse = CustomerResponse.builder()
                .customerId(customerId)
                .customername("Xuan Vuong")
                .address("TP HCM")
                .email("vuong@yopmail.com")
                .phone("0123456789")
                .rewardPoints(10)
                .roles(Set.of(RoleResponse.builder()
                        .name(PredefinedRole.CUSTOMER_ROLE)
                        .description("Customer role")
                        .permissions(Set.of(PermissionResponse.builder()
                                .name("")
                                .description("")
                                .build()))
                        .build()))
                .createdAt(date)
                .updatedAt(date)
                .build();
    }

    @Test
    void createCustomer_validRequest_success() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        CustomerCreationRequest request = CustomerCreationRequest.builder()
                .customername("Xuan Vuong")
                .address("TP HCM")
                .email("vuong@yopmail.com")
                .phone("0123456789")
                .rewardPoints(10)
                .build();
        String content = objectMapper.writeValueAsString(request);

        // giả lập haành vi của lớp service
        when(customerService.createCustomer(any())).thenReturn(customerResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.customerId").value(customerResponse.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.customername").value(customerResponse.getCustomername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.address").value(customerResponse.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(customerResponse.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.phone").value(customerResponse.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.rewardPoints").value(customerResponse.getRewardPoints()));
    }

    @Test
    void getCustomers_validRequest_success() throws Exception {
        when(customerService.getCustomers()).thenReturn(Collections.singletonList(customerResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/customers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customers retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].customerId").value(customerResponse.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].customername").value(customerResponse.getCustomername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].address").value(customerResponse.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].email").value(customerResponse.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].phone").value(customerResponse.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].rewardPoints").value(customerResponse.getRewardPoints()));
    }

    @Test
    void getCustomer_validRequest_success() throws Exception {
        when(customerService.getCustomer(customerId)).thenReturn(customerResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.customerId").value(customerResponse.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.customername").value(customerResponse.getCustomername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.address").value(customerResponse.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(customerResponse.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.phone").value(customerResponse.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.rewardPoints").value(customerResponse.getRewardPoints()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].name").value(PredefinedRole.CUSTOMER_ROLE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].description").value("Customer role"));
    }

    @Test
    void updateCustomer_validRequest_success() throws Exception {
        // Giả lập dữ liệu để trả về sau khi cập nhật
        CustomerUpdateRequest updateRequest = CustomerUpdateRequest.builder()
                .address("TP HCM Updated")
                .email("vuong_updated@yopmail.com")
                .phone("0987654321")
                .rewardPoints(20)
                .build();

        CustomerResponse updatedCustomerResponse = CustomerResponse.builder()
                .customerId(customerId)
                .address(updateRequest.getAddress())
                .email(updateRequest.getEmail())
                .phone(updateRequest.getPhone())
                .rewardPoints(updateRequest.getRewardPoints())
                .roles(customerResponse.getRoles())
                .createdAt(customerResponse.getCreatedAt())
                .updatedAt(date)
                .build();

        String content = objectMapper.writeValueAsString(updateRequest);

        when(customerService.updateCustomer(any(String.class), any(CustomerUpdateRequest.class)))
                .thenReturn(updatedCustomerResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer updated successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.customerId").value(updatedCustomerResponse.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.customername").value(updatedCustomerResponse.getCustomername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.address").value(updatedCustomerResponse.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(updatedCustomerResponse.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.phone").value(updatedCustomerResponse.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.rewardPoints").value(updatedCustomerResponse.getRewardPoints()));
    }

    @Test
    void deleteCustomer_validId_success() throws Exception {
        doNothing().when(customerService).deleteCustomer(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer has been deleted"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Customer has been deleted"));

        // Xác nhận rằng phương thức deleteCustomer đã được gọi một lần với customerId cụ thể
        verify(customerService).deleteCustomer(customerId);
    }

}