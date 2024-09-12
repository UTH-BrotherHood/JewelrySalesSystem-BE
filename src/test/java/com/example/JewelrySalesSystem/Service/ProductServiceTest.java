package com.example.JewelrySalesSystem.Service;

import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductCreationRequest;
import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductUpdateRequest;
import com.example.JewelrySalesSystem.entity.Product;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.ProductMapper;
import com.example.JewelrySalesSystem.repository.CategoryRepository;
import com.example.JewelrySalesSystem.repository.ProductRepository;
import com.example.JewelrySalesSystem.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private ProductMapper productMapper;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private ProductService productService;

  private Product product;
  private ProductCreationRequest creationRequest;
  private ProductUpdateRequest updateRequest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    product = new Product();
    product.setProductId("1");
    product.setName("Test Product");
    product.setCostPrice(BigDecimal.valueOf(100));

    creationRequest = new ProductCreationRequest();
    creationRequest.setName("Test Product");
    creationRequest.setCategoryName("Category1");

    updateRequest = new ProductUpdateRequest();
    updateRequest.setName("Updated Product");
  }

  @Test
  void testCreateProduct_Success() {
    when(categoryRepository.existsById(creationRequest.getCategoryName())).thenReturn(true);
    when(productRepository.existsByName(creationRequest.getName())).thenReturn(false);
    when(productMapper.toProduct(creationRequest)).thenReturn(product);
    when(productRepository.save(product)).thenReturn(product);

    Product result = productService.createProduct(creationRequest);

    assertNotNull(result);
    assertEquals("Test Product", result.getName());
    verify(categoryRepository).existsById(creationRequest.getCategoryName());
    verify(productRepository).existsByName(creationRequest.getName());
    verify(productMapper).toProduct(creationRequest);
    verify(productRepository).save(product);
  }

  @Test
  void testCreateProduct_CategoryNotFound() {
    when(categoryRepository.existsById(creationRequest.getCategoryName())).thenReturn(false);

    AppException thrown = assertThrows(AppException.class, () -> {
      productService.createProduct(creationRequest);
    });

    assertEquals(ErrorCode.CATEGORY_NOT_FOUND, thrown.getErrorCode());
    verify(productRepository, never()).existsByName(creationRequest.getName());
    verify(productMapper, never()).toProduct(creationRequest);
    verify(productRepository, never()).save(any());
  }

  @Test
  void testUpdateProduct_Success() {
    when(productRepository.findById("1")).thenReturn(Optional.of(product));
    // Use doAnswer to handle the updateProduct method
    doAnswer(invocation -> {
      Product p = invocation.getArgument(0);
      ProductUpdateRequest updateRequest = invocation.getArgument(1);
      p.setName(updateRequest.getName());
      return p;
    }).when(productMapper).updateProduct(any(Product.class), any(ProductUpdateRequest.class));
    when(productRepository.save(any(Product.class))).thenReturn(product);

    Product result = productService.updateProduct("1", updateRequest);

    assertNotNull(result);
    assertEquals("Updated Product", result.getName());
    verify(productRepository).findById("1");
    verify(productMapper).updateProduct(any(Product.class), any(ProductUpdateRequest.class));
    verify(productRepository).save(any(Product.class));
  }

  @Test
  void testUpdateProduct_NotFound() {
    when(productRepository.findById("1")).thenReturn(Optional.empty());

    AppException thrown = assertThrows(AppException.class, () -> {
      productService.updateProduct("1", updateRequest);
    });

    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, thrown.getErrorCode());
    verify(productRepository).findById("1");
    verify(productMapper, never()).updateProduct(any(Product.class), any(ProductUpdateRequest.class));
    verify(productRepository, never()).save(any(Product.class));
  }

  @Test
  void testDeleteProduct_Success() {
    when(productRepository.existsById("1")).thenReturn(true);

    productService.deleteProduct("1");

    verify(productRepository).existsById("1");
    verify(productRepository).deleteById("1");
  }

  @Test
  void testDeleteProduct_NotFound() {
    when(productRepository.existsById("1")).thenReturn(false);

    AppException thrown = assertThrows(AppException.class, () -> {
      productService.deleteProduct("1");
    });

    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, thrown.getErrorCode());
    verify(productRepository).existsById("1");
    verify(productRepository, never()).deleteById("1");
  }

//  @Test
//  void testGetProducts() {
//    // Tạo một đối tượng Product giả
//    Product product = new Product();
//    product.setName("Test Product");
//    // Khởi tạo Page chứa sản phẩm
//    Page<Product> page = new PageImpl<>(Collections.singletonList(product));
//
//    // Mô phỏng phương thức findAll với Pageable
//    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
//    when(productRepository.findAll(pageable)).thenReturn(page);
//
//    // Gọi phương thức cần kiểm tra
//    Page<Product> result = productService.getProducts(0, 10, null, null, null, "name", "asc");
//
//    // Kiểm tra kết quả không phải là null
//    assertNotNull(result, "The result should not be null");
//
//    // Kiểm tra số lượng phần tử
//    assertEquals(1, result.getTotalElements(), "Total elements should be 1");
//
//    // Kiểm tra rằng phương thức findAll đã được gọi với đúng Pageable
//    verify(productRepository).findAll(pageable);
//  }
  @Test
  void testGetProduct_Success() {
    when(productRepository.findById("1")).thenReturn(Optional.of(product));

    Product result = productService.getProduct("1");

    assertNotNull(result);
    assertEquals("Test Product", result.getName());
    verify(productRepository).findById("1");
  }

  @Test
  void testGetProduct_NotFound() {
    when(productRepository.findById("1")).thenReturn(Optional.empty());

    AppException thrown = assertThrows(AppException.class, () -> {
      productService.getProduct("1");
    });

    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, thrown.getErrorCode());
    verify(productRepository).findById("1");
  }
}
