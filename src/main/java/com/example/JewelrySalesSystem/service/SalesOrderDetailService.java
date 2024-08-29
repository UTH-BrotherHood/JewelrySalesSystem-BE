package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.SalesOrderDetailRequests.SalesOrderDetailsCreationRequest;
import com.example.JewelrySalesSystem.dto.request.WarrantyRequests.WarrantyCreationRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderDetailResponse;
import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import com.example.JewelrySalesSystem.entity.Product;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.SalesOrderDetailMapper;
import com.example.JewelrySalesSystem.repository.ProductRepository;
import com.example.JewelrySalesSystem.repository.SalesOrderDetailRepository;
import com.example.JewelrySalesSystem.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderDetailService {
    private final SalesOrderDetailRepository salesOrderDetailRepository;
    private final SalesOrderDetailMapper salesOrderDetailMapper;
    private final SalesOrderRepository salesOrderRepository;
    private final ProductRepository productRepository;
    private final WarrantyService warrantyService; // Inject WarrantyService

    @Transactional
    public List<SalesOrderDetailResponse> createSalesOrderDetails(SalesOrderDetailsCreationRequest request) {
        if (!salesOrderRepository.existsById(request.getOrderId())) {
            throw new AppException(ErrorCode.SALES_ORDER_NOT_FOUND);
        }

        List<String> invalidProductIds = request.getProducts().stream()
                .filter(productRequest -> !productRepository.existsById(productRequest.getProductId()))
                .map(productRequest -> productRequest.getProductId())
                .collect(Collectors.toList());

        if (!invalidProductIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        List<String> insufficientStockProductIds = request.getProducts().stream()
                .filter(productRequest -> {
                    Product product = productRepository.findById(productRequest.getProductId()).orElse(null);
                    return product == null || product.getStockQuantity() < productRequest.getQuantity();
                })
                .map(productRequest -> productRequest.getProductId())
                .collect(Collectors.toList());

        if (!insufficientStockProductIds.isEmpty()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        List<SalesOrderDetail> salesOrderDetails = request.getProducts().stream()
                .map(productRequest -> {
                    BigDecimal totalPrice = productRequest.getUnitPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity()));
                    SalesOrderDetail salesOrderDetail = SalesOrderDetail.builder()
                            .orderId(request.getOrderId())
                            .productId(productRequest.getProductId())
                            .quantity(productRequest.getQuantity())
                            .unitPrice(productRequest.getUnitPrice())
                            .totalPrice(totalPrice)
                            .build();
                    return salesOrderDetail;
                }).collect(Collectors.toList());

        List<SalesOrderDetail> savedSalesOrderDetails = salesOrderDetailRepository.saveAll(salesOrderDetails);

        // Create warranties for each product in the order details
        savedSalesOrderDetails.forEach(detail -> {
            WarrantyCreationRequest warrantyRequest = WarrantyCreationRequest.builder()
                    .productId(detail.getProductId())
                    .warrantyStartDate(LocalDateTime.now())
                    .warrantyEndDate(LocalDateTime.now().plusYears(1)) // assuming a 1-year warranty
                    .build();
            warrantyService.createWarranty(warrantyRequest);
        });

        request.getProducts().forEach(productRequest -> {
            Product product = productRepository.findById(productRequest.getProductId()).orElse(null);
            if (product != null) {
                product.setStockQuantity(product.getStockQuantity() - productRequest.getQuantity());
                productRepository.save(product);
            }
        });

        return savedSalesOrderDetails.stream()
                .map(salesOrderDetailMapper::toSalesOrderDetailResponse)
                .collect(Collectors.toList());
    }
}
