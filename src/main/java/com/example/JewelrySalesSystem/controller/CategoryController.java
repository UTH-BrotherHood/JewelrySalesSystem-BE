package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CategoryResponse;
import com.example.JewelrySalesSystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryCreationRequest request) {
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        CategoryResponse categoryResponse = categoryService.createCategory(request);
        apiResponse.setCode(201);
        apiResponse.setMessage("Category created successfully");
        apiResponse.setResult(categoryResponse);
        return apiResponse;
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoryUpdateRequest request) {
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        CategoryResponse categoryResponse = categoryService.updateCategory(categoryId, request);
        apiResponse.setCode(200);
        apiResponse.setMessage("Category updated successfully");
        apiResponse.setResult(categoryResponse);
        return apiResponse;
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<String> deleteCategory(@PathVariable String categoryId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        categoryService.deleteCategory(categoryId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Category deleted successfully");
        apiResponse.setResult("Category has been deleted");
        return apiResponse;
    }

    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable String categoryId) {
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        CategoryResponse categoryResponse = categoryService.getCategory(categoryId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Category retrieved successfully");
        apiResponse.setResult(categoryResponse);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<Page<CategoryResponse>> getCategories(Pageable pageable) {
        ApiResponse<Page<CategoryResponse>> apiResponse = new ApiResponse<>();
        Page<CategoryResponse> categories = categoryService.getCategories(pageable);
        apiResponse.setCode(200);
        apiResponse.setMessage("Categories retrieved successfully");
        apiResponse.setResult(categories);
        return apiResponse;
    }

}
