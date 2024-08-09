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
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryCreationRequest request) {
        CategoryResponse categoryResponse = categoryService.createCategory(request);
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>(201, "Category created successfully", categoryResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoryUpdateRequest request) {
        CategoryResponse categoryResponse = categoryService.updateCategory(categoryId, request);
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>(200, "Category updated successfully", categoryResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Category deleted successfully", null);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable String categoryId) {
        CategoryResponse categoryResponse = categoryService.getCategory(categoryId);
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>(200, "Category retrieved successfully", categoryResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getCategories(Pageable pageable) {
        Page<CategoryResponse> categories = categoryService.getCategories(pageable);
        ApiResponse<Page<CategoryResponse>> apiResponse = new ApiResponse<>(200, "Categories retrieved successfully", categories);
        return ResponseEntity.ok(apiResponse);
    }
}
