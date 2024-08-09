package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.SalesOrderDetailRequests.SalesOrderDetailsCreationRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderDetailResponse;
import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import com.example.JewelrySalesSystem.mapper.SalesOrderDetailMapper;
import com.example.JewelrySalesSystem.repository.SalesOrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderDetailService {
    private final SalesOrderDetailRepository salesOrderDetailRepository;
    private final SalesOrderDetailMapper salesOrderDetailMapper;

    public List<SalesOrderDetailResponse> getSalesOrderDetailsByOrderId(String orderId) {
        List<SalesOrderDetail> salesOrderDetails = salesOrderDetailRepository.findByOrderId(orderId);
        return salesOrderDetails.stream()
                .map(salesOrderDetailMapper::toSalesOrderDetailResponse)
                .collect(Collectors.toList());
    }

    public List<SalesOrderDetailResponse> createSalesOrderDetails(SalesOrderDetailsCreationRequest request) {
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

        return savedSalesOrderDetails.stream()
                .map(salesOrderDetailMapper::toSalesOrderDetailResponse)
                .collect(Collectors.toList());
    }
}
