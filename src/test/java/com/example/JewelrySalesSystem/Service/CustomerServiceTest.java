package com.example.JewelrySalesSystem.Service;

import com.example.JewelrySalesSystem.constant.PredefinedRole;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CustomerResponse;
import com.example.JewelrySalesSystem.entity.Customer;
import com.example.JewelrySalesSystem.entity.Role;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.CustomerMapper;
import com.example.JewelrySalesSystem.repository.CustomerRepository;
import com.example.JewelrySalesSystem.repository.RoleRepository;
import com.example.JewelrySalesSystem.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

  @InjectMocks
  private CustomerService customerService;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private CustomerMapper customerMapper;

  @Mock
  private RoleRepository roleRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateCustomer_Success() {
    // Arrange
    CustomerCreationRequest request = CustomerCreationRequest.builder()
            .customername("JohnDoe")
            .email("john.doe@example.com")
            .phone("+123456789")
            .address("123 Elm Street")
            .rewardPoints(100)
            .build();

    Customer customer = Customer.builder()
            .customername("JohnDoe")
            .email("john.doe@example.com")
            .phone("+123456789")
            .address("123 Elm Street")
            .rewardPoints(100)
            .build();

    Role customerRole = new Role();
    Set<Role> roles = new HashSet<>();
    roles.add(customerRole);

    when(customerRepository.existsBycustomername(request.getCustomername())).thenReturn(false);
    when(customerMapper.toCustomer(request)).thenReturn(customer);
    when(roleRepository.findByName(PredefinedRole.CUSTOMER_ROLE)).thenReturn(Optional.of(customerRole));
    when(customerRepository.save(customer)).thenReturn(customer);

    // Act
    Customer result = customerService.createCustomer(request);

    // Assert
    assertNotNull(result);
    assertEquals("JohnDoe", result.getCustomername());
    verify(customerRepository).existsBycustomername(request.getCustomername());
    verify(customerMapper).toCustomer(request);
    verify(roleRepository).findByName(PredefinedRole.CUSTOMER_ROLE);
    verify(customerRepository).save(customer);
  }

  @Test
  public void testCreateCustomer_UserExists() {
    // Arrange
    CustomerCreationRequest request = CustomerCreationRequest.builder()
            .customername("JohnDoe")
            .build();

    when(customerRepository.existsBycustomername(request.getCustomername())).thenReturn(true);

    // Act & Assert
    AppException thrown = assertThrows(AppException.class, () -> customerService.createCustomer(request));
    assertEquals(ErrorCode.USER_EXISTED, thrown.getErrorCode());
    verify(customerRepository).existsBycustomername(request.getCustomername());
    verifyNoMoreInteractions(customerMapper, roleRepository, customerRepository);
  }

  @Test
  public void testGetCustomers() {
    // Arrange
    Customer customer1 = new Customer();
    Customer customer2 = new Customer();
    List<Customer> customers = Arrays.asList(customer1, customer2);

    when(customerRepository.findAll()).thenReturn(customers);

    // Act
    List<Customer> result = customerService.getCustomers();

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(customerRepository).findAll();
  }

  @Test
  public void testGetCustomer_Success() {
    // Arrange
    String customerId = "customerId";
    Customer customer = new Customer();
    CustomerResponse customerResponse = new CustomerResponse();

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
    when(customerMapper.toCustomerResponse(customer)).thenReturn(customerResponse);

    // Act
    CustomerResponse result = customerService.getCustomer(customerId);

    // Assert
    assertNotNull(result);
    verify(customerRepository).findById(customerId);
    verify(customerMapper).toCustomerResponse(customer);
  }

  @Test
  public void testGetCustomer_UserNotFound() {
    // Arrange
    String customerId = "customerId";

    when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

    // Act & Assert
    AppException thrown = assertThrows(AppException.class, () -> customerService.getCustomer(customerId));
    assertEquals(ErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
    verify(customerRepository).findById(customerId);
    verifyNoMoreInteractions(customerMapper);
  }

  @Test
  public void testUpdateCustomer_Success() {
    // Arrange
    String customerId = "customerId";
    CustomerUpdateRequest request = CustomerUpdateRequest.builder()
            .email("new.email@example.com")
            .phone("+987654321")
            .address("456 Oak Avenue")
            .rewardPoints(200)
            .build();

    Customer existingCustomer = new Customer();
    Customer updatedCustomer = new Customer();
    updatedCustomer.setEmail("new.email@example.com");
    updatedCustomer.setPhone("+987654321");
    updatedCustomer.setAddress("456 Oak Avenue");
    updatedCustomer.setRewardPoints(200);

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
    doAnswer(invocation -> {
      Customer customer = invocation.getArgument(0);
      customer.setEmail(request.getEmail());
      customer.setPhone(request.getPhone());
      customer.setAddress(request.getAddress());
      customer.setRewardPoints(request.getRewardPoints());
      return null;
    }).when(customerMapper).updateCustomer(existingCustomer, request);
    when(customerRepository.save(existingCustomer)).thenReturn(updatedCustomer);

    // Act
    Customer result = customerService.updateCustomer(customerId, request);

    // Assert
    assertNotNull(result);
    assertEquals(updatedCustomer.getEmail(), result.getEmail());
    assertEquals(updatedCustomer.getPhone(), result.getPhone());
    assertEquals(updatedCustomer.getAddress(), result.getAddress());
    assertEquals(updatedCustomer.getRewardPoints(), result.getRewardPoints());
    verify(customerRepository).findById(customerId);
    verify(customerMapper).updateCustomer(existingCustomer, request);
    verify(customerRepository).save(existingCustomer);
  }

  @Test
  public void testUpdateCustomer_UserNotFound() {
    // Arrange
    String customerId = "customerId";
    CustomerUpdateRequest request = new CustomerUpdateRequest();

    when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

    // Act & Assert
    AppException thrown = assertThrows(AppException.class, () -> customerService.updateCustomer(customerId, request));
    assertEquals(ErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
    verify(customerRepository).findById(customerId);
    verifyNoMoreInteractions(customerMapper, customerRepository);
  }

  @Test
  public void testDeleteCustomer_Success() {
    // Arrange
    String customerId = "customerId";

    when(customerRepository.existsById(customerId)).thenReturn(true);
    doNothing().when(customerRepository).deleteById(customerId);

    // Act
    customerService.deleteCustomer(customerId);

    // Assert
    verify(customerRepository).existsById(customerId);
    verify(customerRepository).deleteById(customerId);
  }

  @Test
  public void testDeleteCustomer_UserNotFound() {
    // Arrange
    String customerId = "customerId";

    when(customerRepository.existsById(customerId)).thenReturn(false);

    // Act & Assert
    AppException thrown = assertThrows(AppException.class, () -> customerService.deleteCustomer(customerId));
    assertEquals(ErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
    verify(customerRepository).existsById(customerId);
    verifyNoMoreInteractions(customerRepository);
  }
}
