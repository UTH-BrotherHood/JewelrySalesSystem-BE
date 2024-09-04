package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.PermissionRequests.PermissionRequest;
import com.example.JewelrySalesSystem.dto.response.PermissionResponse;
import com.example.JewelrySalesSystem.service.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource("/test.properties")
@WithMockUser(username = "testUser", roles = {"ADMIN"})

public class PermissionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PermissionService permissionService;

    // tạo trước 2 lớp request và response
    PermissionRequest permissionRequest;
    PermissionResponse permissionResponse;
    ObjectMapper objectMapper;
    final String permissionName = "permission_name_test";
    final String permissionDescription = "permission_description_test";

    @BeforeEach
    void initData(){

        objectMapper = new ObjectMapper();
        permissionRequest = PermissionRequest.builder()
                .name(permissionName)
                .description(permissionDescription)
                .build();

        permissionResponse = PermissionResponse.builder()
                .name(permissionName)
                .description(permissionDescription)
                .build();
    }

    @Test
    void createPermission_validRequest_success() throws Exception {
        String content = objectMapper.writeValueAsString(permissionRequest);

        // Giả lập kết quả cho sự kiện createPermission
        when(permissionService.create(any())).thenReturn(permissionResponse);

        // Giả lập request
        mockMvc.perform(MockMvcRequestBuilders.post("/permissions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Permission created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(permissionResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value(permissionResponse.getDescription()));

    }

    @Test
    void getPermissions_validRequest_success() throws Exception {
        when(permissionService.getAll()).thenReturn(Collections.singletonList(permissionResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/permissions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Permissions retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].name").value(permissionResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].description").value(permissionResponse.getDescription()));

    }

    @Test
    void deletePermission_validRequest_success() throws Exception {
        doNothing().when(permissionService).delete(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/permissions/{permission}", permissionName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Permission deleted successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Permission has been deleted"));

        verify(permissionService).delete(permissionName);
    }


}
