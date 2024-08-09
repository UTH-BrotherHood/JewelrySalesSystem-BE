package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CategoryResponse;
import com.example.JewelrySalesSystem.entity.Category;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.CategoryMapper;
import com.example.JewelrySalesSystem.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryCreationRequest request) {
        Category category = categoryMapper.toCategory(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    public CategoryResponse updateCategory(String categoryId, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryMapper.updateCategory(category, request);
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    public void deleteCategory(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(categoryId);
    }

    public CategoryResponse getCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    public Page<CategoryResponse> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toCategoryResponse);
    }
}
