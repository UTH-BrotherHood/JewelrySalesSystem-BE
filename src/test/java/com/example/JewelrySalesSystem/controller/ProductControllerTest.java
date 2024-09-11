package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.constant.PredefinedRole;
import com.example.JewelrySalesSystem.dto.request.EmployeeRequests.EmployeeUpdateRequest;
import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductCreationRequest;
import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.*;
import com.example.JewelrySalesSystem.service.ProductService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
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

public class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;
    // tạo trước 2 lớp request và response
    ProductCreationRequest productCreationRequest;
    ProductResponse productResponse;
    ObjectMapper objectMapper = new ObjectMapper();
    final String productId = "product_id_test";
    final String categoryId = "category_id_test";

    @BeforeEach
    void initData(){
        productCreationRequest = ProductCreationRequest.builder()
                .categoryId(categoryId)
                .name("Gold Ring")
                .description("A beautiful gold ring")
                .costPrice(BigDecimal.valueOf(100.50))
                .weight(BigDecimal.valueOf(10.5))
                .laborCost(BigDecimal.valueOf(20.0))
                .stoneCost(BigDecimal.valueOf(30.0))
                .stockQuantity(50)
                .build();

        productResponse = ProductResponse.builder()
                .productId(productId)
                .categoryId(categoryId)
                .name("Gold Ring")
                .description("A beautiful gold ring")
                .costPrice(BigDecimal.valueOf(100.50))
                .weight(BigDecimal.valueOf(10.5))
                .laborCost(BigDecimal.valueOf(20.0))
                .stoneCost(BigDecimal.valueOf(30.0))
                .stockQuantity(50)
                .build();
    }

    @Test
    void createProduct_validRequest_success() throws Exception {
        String content = objectMapper.writeValueAsString(productCreationRequest);

        // Giả lập kết quả cho sự kiện createProduct
        when(productService.createProduct(any())).thenReturn(productResponse);

        // Giả lập request
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.productId").value(productResponse.getProductId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.categoryId").value(productResponse.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(productResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value(productResponse.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.costPrice").value(productResponse.getCostPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.weight").value(productResponse.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.laborCost").value(productResponse.getLaborCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.stoneCost").value(productResponse.getStoneCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.stockQuantity").value(productResponse.getStockQuantity()));
    }

    @Test
    void updateProduct_validRequest_success() throws Exception {
        ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder()
                .categoryId(categoryId)
                .name("New Name")
                .description("A beautiful gold ring")
                .costPrice(BigDecimal.valueOf(100.50))
                .weight(BigDecimal.valueOf(10.5))
                .laborCost(BigDecimal.valueOf(20.0))
                .stoneCost(BigDecimal.valueOf(30.0))
                .stockQuantity(50)
                .build();

        String content = objectMapper.writeValueAsString(productUpdateRequest);

        ProductResponse updatedProductResponse = ProductResponse.builder()
                .productId(productId)
                .categoryId(categoryId)
                .name("New Name")
                .description("A beautiful gold ring")
                .costPrice(BigDecimal.valueOf(100.50))
                .weight(BigDecimal.valueOf(10.5))
                .laborCost(BigDecimal.valueOf(20.0))
                .stoneCost(BigDecimal.valueOf(30.0))
                .stockQuantity(50)
                .build();

        when(productService.updateProduct(any(String.class), any(ProductUpdateRequest.class)))
                .thenReturn(updatedProductResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product updated successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.productId").value(updatedProductResponse.getProductId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.categoryId").value(updatedProductResponse.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(updatedProductResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value(updatedProductResponse.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.costPrice").value(updatedProductResponse.getCostPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.weight").value(updatedProductResponse.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.laborCost").value(updatedProductResponse.getLaborCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.stoneCost").value(updatedProductResponse.getStoneCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.stockQuantity").value(updatedProductResponse.getStockQuantity()));


    }

    @Test
    void deleteProduct_validRequest_success() throws Exception {
        doNothing().when(productService).deleteProduct(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{productId}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product deleted successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Product deleted successfully"));

        verify(productService).deleteProduct(productId);
    }

    @Test
    void getProduct_validRequest_success() throws Exception {
        when(productService.getProduct(productId)).thenReturn(productResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.productId").value(productResponse.getProductId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.categoryId").value(productResponse.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(productResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value(productResponse.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.costPrice").value(productResponse.getCostPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.weight").value(productResponse.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.laborCost").value(productResponse.getLaborCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.stoneCost").value(productResponse.getStoneCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.stockQuantity").value(productResponse.getStockQuantity()));
    }

    @Test
    void getProducts_validRequest_success() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductResponse> products = new PageImpl<>(Collections.singletonList(productResponse), pageable, 1);

        when(productService.getProducts(
                eq(0),
                eq(10),
                isNull(),
                isNull(),
                isNull(),
                eq("createdAt"),
                eq("asc")
        )).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Products retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].productId").value(productResponse.getProductId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].categoryId").value(productResponse.getCategoryId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].name").value(productResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].description").value(productResponse.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].costPrice").value(productResponse.getCostPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].weight").value(productResponse.getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].laborCost").value(productResponse.getLaborCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].stoneCost").value(productResponse.getStoneCost()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].stockQuantity").value(productResponse.getStockQuantity()));
    }


}
