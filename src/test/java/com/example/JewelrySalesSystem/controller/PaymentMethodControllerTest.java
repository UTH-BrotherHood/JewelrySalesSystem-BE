package com.example.JewelrySalesSystem.controller;


import com.example.JewelrySalesSystem.constant.PredefinedRole;
import com.example.JewelrySalesSystem.dto.request.PaymentMethodCreationRequest;
import com.example.JewelrySalesSystem.dto.response.PaymentMethodResponse;
import com.example.JewelrySalesSystem.service.PaymentMethodService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource("/test.properties")
@WithMockUser(username = "testUser", roles = {"ADMIN"})
public class PaymentMethodControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PaymentMethodService paymentMethodService;

    // tạo trước 2 lớp request và response
    PaymentMethodCreationRequest paymentMethodCreationRequest;
    PaymentMethodResponse paymentMethodResponse;
    ObjectMapper objectMapper;
    final String paymentMethodId = "paymentMethod_id_test";
    final String paymentMethodName = "Credit Card";
    final String details = "Visa and MasterCard";

    @BeforeEach
    void initData(){
        objectMapper = new ObjectMapper();

        paymentMethodCreationRequest = PaymentMethodCreationRequest.builder()
                .paymentMethodName(paymentMethodName)
                .details(details)
                .active(true)
                .build();

        paymentMethodResponse = PaymentMethodResponse.builder()
                .paymentMethodId(paymentMethodId)
                .paymentMethodName(paymentMethodName)
                .details(details)
                .active(true)
                .build();
    }

    @Test
    void createPaymentMethod_validRequest_success() throws Exception {
        String content = objectMapper.writeValueAsString(paymentMethodCreationRequest);

        // Giả lập kết quả cho sự kiện createPaymentMethod
        when(paymentMethodService.createPaymentMethod(any())).thenReturn(paymentMethodResponse);

        // Giả lập request
        mockMvc.perform(MockMvcRequestBuilders.post("/payment-methods/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Payment method created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.paymentMethodId").value(paymentMethodResponse.getPaymentMethodId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.paymentMethodName").value(paymentMethodResponse.getPaymentMethodName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.details").value(paymentMethodResponse.getDetails()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.active").value(true));


    }

    @Test
    void getPaymentMethod_validRequest_success() throws Exception {
        when(paymentMethodService.getPaymentMethod(paymentMethodId)).thenReturn(paymentMethodResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/payment-methods/{paymentMethodId}", paymentMethodId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Payment method retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.paymentMethodId").value(paymentMethodResponse.getPaymentMethodId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.paymentMethodName").value(paymentMethodResponse.getPaymentMethodName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.details").value(paymentMethodResponse.getDetails()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.active").value(true));


    }
}
