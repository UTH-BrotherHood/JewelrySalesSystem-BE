package com.example.JewelrySalesSystem.Service;

import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CategoryRequests.CategoryUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CategoryResponse;
import com.example.JewelrySalesSystem.entity.Category;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.CategoryMapper;
import com.example.JewelrySalesSystem.repository.CategoryRepository;
import com.example.JewelrySalesSystem.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

  @InjectMocks
  private CategoryService categoryService;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private CategoryMapper categoryMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateCategory_Success() {
    // Arrange
    CategoryCreationRequest request = CategoryCreationRequest.builder()
            .categoryName("Jewelry")
            .description("Category for jewelry")
            .build();

    Category category = Category.builder()
            .categoryName("Jewelry")
            .description("Category for jewelry")
            .build();

    Category savedCategory = Category.builder()
            .categoryName("Jewelry")
            .description("Category for jewelry")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    CategoryResponse categoryResponse = CategoryResponse.builder()
            .categoryName("Jewelry")
            .description("Category for jewelry")
            .createdAt(savedCategory.getCreatedAt())
            .updatedAt(savedCategory.getUpdatedAt())
            .build();

    when(categoryRepository.existsById(request.getCategoryName())).thenReturn(false);
    when(categoryMapper.toCategory(request)).thenReturn(category);
    when(categoryRepository.save(category)).thenReturn(savedCategory);
    when(categoryMapper.toCategoryResponse(savedCategory)).thenReturn(categoryResponse);

    // Act
    CategoryResponse result = categoryService.createCategory(request);

    // Assert
    assertNotNull(result);
    assertEquals("Jewelry", result.getCategoryName());
    verify(categoryRepository).existsById(request.getCategoryName());
    verify(categoryMapper).toCategory(request);
    verify(categoryRepository).save(category);
    verify(categoryMapper).toCategoryResponse(savedCategory);
  }

  @Test
  public void testCreateCategory_CategoryExists() {
    // Arrange
    CategoryCreationRequest request = CategoryCreationRequest.builder()
            .categoryName("Jewelry")
            .build();

    when(categoryRepository.existsById(request.getCategoryName())).thenReturn(true);

    // Act & Assert
    AppException thrown = assertThrows(AppException.class, () -> categoryService.createCategory(request));
    assertEquals(ErrorCode.CATEGORY_ALREADY_EXISTS, thrown.getErrorCode());
    verify(categoryRepository).existsById(request.getCategoryName());
    verifyNoMoreInteractions(categoryMapper, categoryRepository);
  }

  @Test
  public void testUpdateCategory_Success() {
    // Arrange
    String categoryName = "Jewelry";
    CategoryUpdateRequest request = CategoryUpdateRequest.builder()
            .description("Updated description")
            .build();

    Category existingCategory = Category.builder()
            .categoryName(categoryName)
            .description("Old description")
            .build();

    Category updatedCategory = Category.builder()
            .categoryName(categoryName)
            .description("Updated description")
            .createdAt(existingCategory.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

    CategoryResponse categoryResponse = CategoryResponse.builder()
            .categoryName(categoryName)
            .description("Updated description")
            .createdAt(updatedCategory.getCreatedAt())
            .updatedAt(updatedCategory.getUpdatedAt())
            .build();

    when(categoryRepository.findById(categoryName)).thenReturn(Optional.of(existingCategory));
    doAnswer(invocation -> {
      Category category = invocation.getArgument(0);
      category.setDescription(request.getDescription());
      category.setUpdatedAt(LocalDateTime.now());
      return null;
    }).when(categoryMapper).updateCategory(existingCategory, request);
    when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
    when(categoryMapper.toCategoryResponse(updatedCategory)).thenReturn(categoryResponse);

    // Act
    CategoryResponse result = categoryService.updateCategory(categoryName, request);

    // Assert
    assertNotNull(result);
    assertEquals("Updated description", result.getDescription());
    verify(categoryRepository).findById(categoryName);
    verify(categoryMapper).updateCategory(existingCategory, request);
    verify(categoryRepository).save(existingCategory);
    verify(categoryMapper).toCategoryResponse(updatedCategory);
  }

  @Test
  public void testUpdateCategory_CategoryNotFound() {
    // Arrange
    String categoryName = "Jewelry";
    CategoryUpdateRequest request = new CategoryUpdateRequest();

    when(categoryRepository.findById(categoryName)).thenReturn(Optional.empty());

    // Act & Assert
    AppException thrown = assertThrows(AppException.class, () -> categoryService.updateCategory(categoryName, request));
    assertEquals(ErrorCode.CATEGORY_NOT_FOUND, thrown.getErrorCode());
    verify(categoryRepository).findById(categoryName);
    verifyNoMoreInteractions(categoryMapper, categoryRepository);
  }

  @Test
  public void testDeleteCategory_Success() {
    // Arrange
    String categoryName = "Jewelry";

    when(categoryRepository.existsById(categoryName)).thenReturn(true);
    doNothing().when(categoryRepository).deleteById(categoryName);

    // Act
    categoryService.deleteCategory(categoryName);

    // Assert
    verify(categoryRepository).existsById(categoryName);
    verify(categoryRepository).deleteById(categoryName);
  }

  @Test
  public void testDeleteCategory_CategoryNotFound() {
    // Arrange
    String categoryName = "Jewelry";

    when(categoryRepository.existsById(categoryName)).thenReturn(false);

    // Act & Assert
    AppException thrown = assertThrows(AppException.class, () -> categoryService.deleteCategory(categoryName));
    assertEquals(ErrorCode.CATEGORY_NOT_FOUND, thrown.getErrorCode());
    verify(categoryRepository).existsById(categoryName);
    verifyNoMoreInteractions(categoryRepository);
  }

  @Test
  public void testGetCategory_Success() {
    // Arrange
    String categoryName = "Jewelry";
    Category category = Category.builder()
            .categoryName(categoryName)
            .description("Category for jewelry")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    CategoryResponse categoryResponse = CategoryResponse.builder()
            .categoryName(categoryName)
            .description("Category for jewelry")
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();

    when(categoryRepository.findById(categoryName)).thenReturn(Optional.of(category));
    when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

    // Act
    CategoryResponse result = categoryService.getCategory(categoryName);

    // Assert
    assertNotNull(result);
    assertEquals("Jewelry", result.getCategoryName());
    verify(categoryRepository).findById(categoryName);
    verify(categoryMapper).toCategoryResponse(category);
  }

  @Test
  public void testGetCategory_CategoryNotFound() {
    // Arrange
    String categoryName = "Jewelry";

    when(categoryRepository.findById(categoryName)).thenReturn(Optional.empty());

    // Act & Assert
    AppException thrown = assertThrows(AppException.class, () -> categoryService.getCategory(categoryName));
    assertEquals(ErrorCode.CATEGORY_NOT_FOUND, thrown.getErrorCode());
    verify(categoryRepository).findById(categoryName);
    verifyNoMoreInteractions(categoryMapper);
  }

  @Test
  public void testGetCategories() {
    // Arrange
    Pageable pageable = Pageable.unpaged();
    Category category1 = Category.builder().categoryName("Jewelry").build();
    Category category2 = Category.builder().categoryName("Watches").build();
    Page<Category> categoryPage = new PageImpl<>(Arrays.asList(category1, category2), pageable, 2);

    CategoryResponse categoryResponse1 = CategoryResponse.builder().categoryName("Jewelry").build();
    CategoryResponse categoryResponse2 = CategoryResponse.builder().categoryName("Watches").build();
    Page<CategoryResponse> categoryResponsePage = new PageImpl<>(Arrays.asList(categoryResponse1, categoryResponse2), pageable, 2);

    when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
    when(categoryMapper.toCategoryResponse(category1)).thenReturn(categoryResponse1);
    when(categoryMapper.toCategoryResponse(category2)).thenReturn(categoryResponse2);

    // Act
    Page<CategoryResponse> result = categoryService.getCategories(pageable);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals("Jewelry", result.getContent().get(0).getCategoryName());
    assertEquals("Watches", result.getContent().get(1).getCategoryName());
    verify(categoryRepository).findAll(pageable);
    verify(categoryMapper).toCategoryResponse(category1);
    verify(categoryMapper).toCategoryResponse(category2);
  }
}
