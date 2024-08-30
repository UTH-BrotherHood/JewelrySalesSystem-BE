package com.example.JewelrySalesSystem.Service;

import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderCreationRequest;
import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderResponse;
import com.example.JewelrySalesSystem.dto.response.SalesOrderWithDetailsResponse;
import com.example.JewelrySalesSystem.entity.SalesOrder;
import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.SalesOrderMapper;
import com.example.JewelrySalesSystem.repository.CustomerRepository;
import com.example.JewelrySalesSystem.repository.EmployeeRepository;
import com.example.JewelrySalesSystem.repository.SalesOrderDetailRepository;
import com.example.JewelrySalesSystem.repository.SalesOrderRepository;
import com.example.JewelrySalesSystem.service.SalesOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SalesOrderServiceTest {

  @Mock
  private SalesOrderRepository salesOrderRepository;

  @Mock
  private SalesOrderMapper salesOrderMapper;

  @Mock
  private SalesOrderDetailRepository salesOrderDetailRepository;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @InjectMocks
  private SalesOrderService salesOrderService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createSalesOrder_Success() {
    SalesOrderCreationRequest request = new SalesOrderCreationRequest(
            "customerId", "employeeId", LocalDateTime.now(), BigDecimal.valueOf(100.00)
    );
    SalesOrder salesOrder = new SalesOrder();
    SalesOrder savedSalesOrder = new SalesOrder();
    SalesOrderResponse response = new SalesOrderResponse();

    when(customerRepository.existsById(request.getCustomerId())).thenReturn(true);
    when(employeeRepository.existsById(request.getEmployeeId())).thenReturn(true);
    when(salesOrderMapper.toSalesOrder(request)).thenReturn(salesOrder);
    when(salesOrderRepository.save(salesOrder)).thenReturn(savedSalesOrder);
    when(salesOrderMapper.toSalesOrderResponse(savedSalesOrder)).thenReturn(response);

    SalesOrderResponse result = salesOrderService.createSalesOrder(request);

    assertNotNull(result);
    verify(salesOrderRepository).save(salesOrder);
  }

  @Test
  void createSalesOrder_CustomerNotFound() {
    SalesOrderCreationRequest request = new SalesOrderCreationRequest(
            "customerId", "employeeId", LocalDateTime.now(), BigDecimal.valueOf(100.00)
    );

    when(customerRepository.existsById(request.getCustomerId())).thenReturn(false);

    AppException exception = assertThrows(AppException.class, () -> salesOrderService.createSalesOrder(request));
    assertEquals(ErrorCode.CUSTOMER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void updateSalesOrder_Success() {
    SalesOrderUpdateRequest request = new SalesOrderUpdateRequest(
            "customerId", "employeeId", LocalDateTime.now(), BigDecimal.valueOf(150.00)
    );
    SalesOrder salesOrder = new SalesOrder();
    SalesOrder updatedSalesOrder = new SalesOrder();
    SalesOrderResponse response = new SalesOrderResponse();

    when(salesOrderRepository.findById(anyString())).thenReturn(Optional.of(salesOrder));
    when(customerRepository.existsById(request.getCustomerId())).thenReturn(true);
    when(employeeRepository.existsById(request.getEmployeeId())).thenReturn(true);
    // Mock để phương thức updateSalesOrder không thay đổi salesOrder
    when(salesOrderMapper.toSalesOrderResponse(updatedSalesOrder)).thenReturn(response);
    when(salesOrderRepository.save(salesOrder)).thenReturn(updatedSalesOrder);

    SalesOrderResponse result = salesOrderService.updateSalesOrder("orderId", request);

    assertNotNull(result);
    verify(salesOrderRepository).save(salesOrder);
  }

  @Test
  void getSalesOrderWithDetails_Success() {
    SalesOrder salesOrder = new SalesOrder(); // Đảm bảo đối tượng này đã được khởi tạo
    List<SalesOrderDetail> details = List.of(new SalesOrderDetail());
    SalesOrderResponse salesOrderResponse = new SalesOrderResponse(); // Khởi tạo SalesOrderResponse nếu cần

    SalesOrderWithDetailsResponse response = SalesOrderWithDetailsResponse.builder()
            .salesOrder(salesOrderResponse) // Đảm bảo bạn sử dụng đối tượng SalesOrderResponse hợp lệ
            .orderDetails(details) // Sử dụng orderDetails
            .build();

    // Giả lập hành vi của phương thức Mapper
    when(salesOrderRepository.findById(anyString())).thenReturn(Optional.of(salesOrder));
    when(salesOrderDetailRepository.findByOrderId(anyString())).thenReturn(details);
    when(salesOrderMapper.toSalesOrderWithDetailsResponse(any(SalesOrder.class), anyList())).thenReturn(response); // Sử dụng any(SalesOrder.class) và anyList()

    SalesOrderWithDetailsResponse result = salesOrderService.getSalesOrderWithDetails("orderId");

    assertNotNull(result);
    assertEquals(details, result.getOrderDetails());
  }


}
