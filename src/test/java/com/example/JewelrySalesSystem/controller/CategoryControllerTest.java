package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CategoryResponse;
import com.example.JewelrySalesSystem.service.CategoryService;
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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource("/test.properties")
@WithMockUser(username = "testUser", roles = {"ADMIN"})
class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    // Tạo trước 2 lớp request và response
    CategoryCreationRequest categoryCreationRequest;
    CategoryResponse categoryResponse;
    ObjectMapper objectMapper;
    LocalDateTime date;
    final String categoryId = "category_id_test";

    @BeforeEach
    void initData() {
        date = LocalDateTime.of(2024, Month.AUGUST, 30, 0, 0, 0);
        objectMapper = new ObjectMapper();

        categoryCreationRequest = CategoryCreationRequest.builder()
                .categoryName("Jewelry")
                .description("Jewelry is good for life")
                .build();

        categoryResponse = CategoryResponse.builder()
                .categoryName("Jewelry")
                .description("Jewelry is good for life")
                .createdAt(date)
                .updatedAt(date)
                .build();
    }

    @Test
    void createCategory_validRequest_success() throws Exception {
        objectMapper.registerModule(new JavaTimeModule()); // Đăng ký module để mapper có thể serialize được LocalDateTime
        String content = objectMapper.writeValueAsString(categoryCreationRequest);

        // Giả lập kết quả cho sự kiện createCategory
        when(categoryService.createCategory(any())).thenReturn(categoryResponse);

        // Giả lập request, ko check ngày vì sợ sau này có thể thay đổi format, chủ yếu mình check những dữ liệu khác như statuscode, content.
        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.categoryName").value("Jewelry"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value("Jewelry is good for life"));
    }

    @Test
    void createCategory_categoryNameInvalid_badRequest() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        categoryCreationRequest.setCategoryName("");
        String invalidContent = objectMapper.writeValueAsString(categoryCreationRequest);

        // Giả lập request nếu ko có categoryName
        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(invalidContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.categoryName").value("Category name is required"));
    }

    @Test
    void updateCategory_validRequest_success() throws Exception {
        CategoryUpdateRequest categoryUpdateRequest = CategoryUpdateRequest.builder()
                .categoryName("Updated Jewelry")
                .description("Updated description")
                .build();
        String content = objectMapper.writeValueAsString(categoryUpdateRequest);

        CategoryResponse updatedCategoryResponse = CategoryResponse.builder()
                .categoryName("Updated Jewelry")
                .description("Updated description")
                .createdAt(date)
                .updatedAt(date)
                .build();

        when(categoryService.updateCategory(any(String.class), any(CategoryUpdateRequest.class)))
                .thenReturn(updatedCategoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category updated successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.categoryName").value(categoryUpdateRequest.getCategoryName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value(categoryUpdateRequest.getDescription()));
    }

    @Test
    void deleteCategory_validRequest_success() throws Exception {
        doNothing().when(categoryService).deleteCategory(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/{categoryId}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category deleted successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Category has been deleted"));

        verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    void getCategory_validRequest_success() throws Exception {
        when(categoryService.getCategory(any())).thenReturn(categoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/{categoryId}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.categoryName").value("Jewelry"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value("Jewelry is good for life"));
    }

    @Test
    void getCategories_validRequest_success() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryResponse> categories = new PageImpl<>(Collections.singletonList(categoryResponse), pageable, 1);

        when(categoryService.getCategories(any(Pageable.class))).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Categories retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].categoryName").value("Jewelry"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].description").value("Jewelry is good for life"));
    }



}