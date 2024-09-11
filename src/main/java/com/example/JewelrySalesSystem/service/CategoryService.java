package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CategoryResponse;
import com.example.JewelrySalesSystem.entity.Category;
import com.example.JewelrySalesSystem.entity.Product;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.CategoryMapper;
import com.example.JewelrySalesSystem.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        // Kiểm tra xem categoryName đã tồn tại chưa
        if (categoryRepository.existsByCategoryName(request.getCategoryName())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = categoryMapper.toCategory(request);

        Category savedCategory = categoryRepository.save(category);

        CategoryResponse response = categoryMapper.toCategoryResponse(savedCategory);

        return response;
    }


    public CategoryResponse updateCategory(String categoryName, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(categoryName)
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

    public CategoryResponse getCategory(String categoryName) {
        Category category = categoryRepository.findById(categoryName)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    public Page<CategoryResponse> getCategories(Pageable pageable, String categoryName) {
        Specification<Category> spec = Specification.where(null);

        if (categoryName != null && !categoryName.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("categoryName"), "%" + categoryName + "%"));
        }

        return categoryRepository.findAll(spec, pageable)
                .map(categoryMapper::toCategoryResponse);
    }


}
