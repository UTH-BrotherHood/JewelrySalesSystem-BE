package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderCreationRequest;
import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderUpdateRequest;
import com.example.JewelrySalesSystem.dto.request.WarrantyRequests.WarrantyCreationRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderResponse;
import com.example.JewelrySalesSystem.dto.response.SalesOrderWithDetailsResponse;

import com.example.JewelrySalesSystem.entity.*;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.SalesOrderMapper;
import com.example.JewelrySalesSystem.mapper.WarrantyMapper;
import com.example.JewelrySalesSystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderService {
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderMapper salesOrderMapper;
    private final SalesOrderDetailRepository salesOrderDetailRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final WarrantyRepository warrantyRepository;
    private final WarrantyMapper warrantyMapper;
    private final WarrantyService warrantyService;
    private final CartService cartService;

    @Transactional
    public SalesOrderResponse createSalesOrder(SalesOrderCreationRequest request) {
        // Check if customer and employee exist
        if (!customerRepository.existsById(request.getCustomerId())) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        // Retrieve Cart
        Cart cart = cartRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Check if cart has items
        if (cart.getItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_IS_EMPTY); // Custom error code for empty cart
        }

        // Calculate totalAmount from Cart items
        BigDecimal totalAmount = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create SalesOrder and link to Cart
        SalesOrder salesOrder = salesOrderMapper.toSalesOrder(request);
        salesOrder.setCartId(cart.getCartId());
        salesOrder.setOrderDate(LocalDateTime.now()); // Set orderDate to current date and time
        salesOrder.setTotalAmount(totalAmount); // Set totalAmount based on cart items
        SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);

        // Create SalesOrderDetails from Cart items
        createSalesOrderDetailsFromCart(savedSalesOrder.getOrderId(), cart);

        // Clear the cart after creating the sales order
        cartService.clearCart(request.getCustomerId());

        return salesOrderMapper.toSalesOrderResponse(savedSalesOrder);
    }


    private void createSalesOrderDetailsFromCart(String orderId, Cart cart) {
        List<CartItem> cartItems = cart.getItems();

        // Check if all products in the cart exist and have sufficient stock
        List<String> invalidProductIds = cartItems.stream()
                .filter(cartItem -> !productRepository.existsById(cartItem.getProductId()))
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        if (!invalidProductIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        List<String> insufficientStockProductIds = cartItems.stream()
                .filter(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
                    return product == null || product.getStockQuantity() < cartItem.getQuantity();
                })
                .map(CartItem::getProductId)
                .collect(Collectors.toList());

        if (!insufficientStockProductIds.isEmpty()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // Create SalesOrderDetails
        List<SalesOrderDetail> salesOrderDetails = cartItems.stream()
                .map(cartItem -> {
                    BigDecimal totalPrice = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                    return SalesOrderDetail.builder()
                            .orderId(orderId)
                            .productId(cartItem.getProductId())
                            .quantity(cartItem.getQuantity())
                            .unitPrice(cartItem.getPrice())
                            .totalPrice(totalPrice)
                            .build();
                }).collect(Collectors.toList());

        salesOrderDetailRepository.saveAll(salesOrderDetails);

        // Update product stock and create warranties
        cartItems.forEach(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
            if (product != null) {
                product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
                productRepository.save(product);

                // Create warranty
                WarrantyCreationRequest warrantyRequest = WarrantyCreationRequest.builder()
                        .productId(cartItem.getProductId())
                        .warrantyStartDate(LocalDateTime.now())
                        .warrantyEndDate(LocalDateTime.now().plusYears(1))
                        .build();
                warrantyService.createWarranty(warrantyRequest);
            }
        });
    }



    public void deleteSalesOrder(String orderId) {
        if (!salesOrderRepository.existsById(orderId)) {
            throw new AppException(ErrorCode.SALES_ORDER_NOT_FOUND);
        }
        salesOrderRepository.deleteById(orderId);
    }

    public SalesOrderResponse getSalesOrder(String orderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOT_FOUND));
        return salesOrderMapper.toSalesOrderResponse(salesOrder);
    }

    public Page<SalesOrderResponse> getSalesOrders(Pageable pageable) {
        return salesOrderRepository.findAll(pageable)
                .map(salesOrderMapper::toSalesOrderResponse);
    }

    public Page<SalesOrderResponse> getSalesOrdersByEmployeeId(String employeeId, Pageable pageable) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        return salesOrderRepository.findByEmployeeId(employeeId, pageable)
                .map(salesOrderMapper::toSalesOrderResponse);
    }

    public Page<SalesOrderResponse> getSalesOrdersByCustomerId(String customerId, Pageable pageable) {
        if (!customerRepository.existsById(customerId)) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        return salesOrderRepository.findByCustomerId(customerId, pageable)
                .map(salesOrderMapper::toSalesOrderResponse);
    }

    public SalesOrderWithDetailsResponse getSalesOrderWithDetails(String orderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOT_FOUND));

        List<SalesOrderDetail> orderDetails = salesOrderDetailRepository.findByOrderId(orderId);

        return SalesOrderWithDetailsResponse.builder()
                .salesOrder(salesOrderMapper.toSalesOrderResponse(salesOrder))
                .orderDetails(orderDetails)
                .build();
    }
}
