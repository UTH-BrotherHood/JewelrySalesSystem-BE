package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.AuthenticationRequest;
import com.example.JewelrySalesSystem.dto.request.IntrospectRequest;
import com.example.JewelrySalesSystem.dto.request.LogoutRequest;
import com.example.JewelrySalesSystem.dto.response.AuthenticationResponse;
import com.example.JewelrySalesSystem.dto.response.IntrospectResponse;
import com.example.JewelrySalesSystem.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;
    private IntrospectRequest introspectRequest;
    private IntrospectResponse introspectResponse;
    private LogoutRequest logoutRequest;

    @BeforeEach
    void initData() {
        authenticationRequest = AuthenticationRequest.builder()
                .username("admin")
                .password("admin")
                .build();

        authenticationResponse = AuthenticationResponse.builder()
                .authenticated(true)
                .token("sampleToken123")
                .build();

        introspectRequest = IntrospectRequest.builder()
                .token("sampleToken123")
                .build();

        introspectResponse = IntrospectResponse.builder()
                .valid(true)
                .build();

        logoutRequest = LogoutRequest.builder()
                .token("sampleToken123")
                .build();
    }

    @Test
    void authenticate_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(authenticationRequest);

        Mockito.when(authenticationService.authenticate(ArgumentMatchers.any(AuthenticationRequest.class)))
                .thenReturn(authenticationResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.token").value("sampleToken123"));
    }



    @Test
    void introspect_unauthorized() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(introspectRequest);

        // Mock the introspect method to return a response indicating the token is invalid
        IntrospectResponse invalidResponse = new IntrospectResponse();
        invalidResponse.setValid(false); //

        Mockito.when(authenticationService.introspect(ArgumentMatchers.any(IntrospectRequest.class)))
                .thenReturn(invalidResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/introspect")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()) // 401
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Authentication failed: Token expired or invalid"));
    }

    @Test
    void logout_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(logoutRequest);

        Mockito.doNothing().when(authenticationService).logout(ArgumentMatchers.any(LogoutRequest.class));

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/log-out")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void logout_unclassifiedException_failure() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(logoutRequest);

        // Giả lập lỗi không phân loại khi gọi phương thức logout
        Mockito.doThrow(new RuntimeException("Uncategorized exception")).when(authenticationService).logout(ArgumentMatchers.any(LogoutRequest.class));

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/log-out")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError()) // 500 hoặc mã lỗi tương ứng
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Uncategorized exception"));
    }


}