package com.example.JewelrySalesSystem.Service;

import com.example.JewelrySalesSystem.dto.request.SalesOrderDetailRequests.SalesOrderDetailCreationRequestz;
import com.example.JewelrySalesSystem.dto.request.SalesOrderDetailRequests.SalesOrderDetailsCreationRequest;
import com.example.JewelrySalesSystem.dto.request.WarrantyRequests.WarrantyCreationRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderDetailResponse;
import com.example.JewelrySalesSystem.entity.Product;
import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.SalesOrderDetailMapper;
import com.example.JewelrySalesSystem.repository.ProductRepository;
import com.example.JewelrySalesSystem.repository.SalesOrderDetailRepository;
import com.example.JewelrySalesSystem.repository.SalesOrderRepository;
import com.example.JewelrySalesSystem.service.SalesOrderDetailService;
import com.example.JewelrySalesSystem.service.WarrantyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesOrderDetailServiceTest {

  @Mock
  private SalesOrderDetailRepository salesOrderDetailRepository;

  @Mock
  private SalesOrderDetailMapper salesOrderDetailMapper;

  @Mock
  private SalesOrderRepository salesOrderRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private WarrantyService warrantyService;

  @InjectMocks
  private SalesOrderDetailService salesOrderDetailService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateSalesOrderDetails_Success() {
    // Arrange
    SalesOrderDetailCreationRequestz productRequest = SalesOrderDetailCreationRequestz.builder()
            .productId("product1")
            .quantity(2)
            .unitPrice(new BigDecimal("100.00"))
            .build();

    SalesOrderDetailsCreationRequest request = SalesOrderDetailsCreationRequest.builder()
            .orderId("order1")
            .products(List.of(productRequest))
            .build();

    Product product = new Product();
    product.setProductId("product1");
    product.setStockQuantity(10);

    SalesOrderDetail salesOrderDetail = SalesOrderDetail.builder()
            .orderId("order1")
            .productId("product1")
            .quantity(2)
            .unitPrice(new BigDecimal("100.00"))
            .totalPrice(new BigDecimal("200.00"))
            .build();

    when(salesOrderRepository.existsById(request.getOrderId())).thenReturn(true);
    when(productRepository.existsById(productRequest.getProductId())).thenReturn(true);
    doReturn(Optional.of(product)).when(productRepository).findById(productRequest.getProductId());
    when(salesOrderDetailRepository.saveAll(anyList())).thenReturn(List.of(salesOrderDetail));
    when(salesOrderDetailMapper.toSalesOrderDetailResponse(salesOrderDetail)).thenReturn(new SalesOrderDetailResponse());

    List<SalesOrderDetailResponse> responses = salesOrderDetailService.createSalesOrderDetails(request);

    assertNotNull(responses);
    assertEquals(1, responses.size());

    verify(salesOrderRepository, times(1)).existsById(request.getOrderId());
    verify(productRepository, times(1)).existsById(productRequest.getProductId());
    verify(productRepository, times(2)).findById(productRequest.getProductId());
    verify(salesOrderDetailRepository, times(1)).saveAll(anyList());
    verify(warrantyService, times(1)).createWarranty(any(WarrantyCreationRequest.class));
    verify(productRepository, times(1)).save(product);
  }


  @Test
  void testCreateSalesOrderDetails_SalesOrderNotFound() {
    // Arrange
    SalesOrderDetailsCreationRequest request = SalesOrderDetailsCreationRequest.builder()
            .orderId("order1")
            .products(Collections.emptyList())
            .build();

    when(salesOrderRepository.existsById(request.getOrderId())).thenReturn(false);

    AppException exception = assertThrows(AppException.class, () ->
            salesOrderDetailService.createSalesOrderDetails(request));

    assertEquals(ErrorCode.SALES_ORDER_NOT_FOUND, exception.getErrorCode());
    verify(salesOrderRepository, times(1)).existsById(request.getOrderId());
  }

  @Test
  void testCreateSalesOrderDetails_ProductNotFound() {
    // Arrange
    SalesOrderDetailCreationRequestz productRequest = SalesOrderDetailCreationRequestz.builder()
            .productId("product1")
            .quantity(2)
            .unitPrice(new BigDecimal("100.00"))
            .build();

    SalesOrderDetailsCreationRequest request = SalesOrderDetailsCreationRequest.builder()
            .orderId("order1")
            .products(List.of(productRequest))
            .build();

    when(salesOrderRepository.existsById(request.getOrderId())).thenReturn(true);
    when(productRepository.existsById(productRequest.getProductId())).thenReturn(false);

    AppException exception = assertThrows(AppException.class, () ->
            salesOrderDetailService.createSalesOrderDetails(request));

    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    verify(productRepository, times(1)).existsById(productRequest.getProductId());
  }

  @Test
  void testCreateSalesOrderDetails_InsufficientStock() {
    // Arrange
    SalesOrderDetailCreationRequestz productRequest = SalesOrderDetailCreationRequestz.builder()
            .productId("product1")
            .quantity(20)
            .unitPrice(new BigDecimal("100.00"))
            .build();

    SalesOrderDetailsCreationRequest request = SalesOrderDetailsCreationRequest.builder()
            .orderId("order1")
            .products(List.of(productRequest))
            .build();

    Product product = new Product();
    product.setProductId("product1");
    product.setStockQuantity(10);

    when(salesOrderRepository.existsById(request.getOrderId())).thenReturn(true);
    when(productRepository.existsById(productRequest.getProductId())).thenReturn(true);
    when(productRepository.findById(productRequest.getProductId())).thenReturn(Optional.of(product));

    // Act & Assert
    AppException exception = assertThrows(AppException.class, () ->
            salesOrderDetailService.createSalesOrderDetails(request));

    assertEquals(ErrorCode.INSUFFICIENT_STOCK, exception.getErrorCode());
    verify(productRepository, times(1)).findById(productRequest.getProductId());
  }
}
