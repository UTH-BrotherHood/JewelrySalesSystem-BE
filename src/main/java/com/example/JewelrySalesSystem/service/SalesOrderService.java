package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderCreationRequest;
import com.example.JewelrySalesSystem.dto.request.SalesOrderRequests.SalesOrderUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.SalesOrderResponse;
import com.example.JewelrySalesSystem.dto.response.SalesOrderWithDetailsResponse;
import com.example.JewelrySalesSystem.entity.SalesOrder;
import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.SalesOrderMapper;
import com.example.JewelrySalesSystem.repository.SalesOrderDetailRepository;
import com.example.JewelrySalesSystem.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderService {
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderMapper salesOrderMapper;
    private final SalesOrderDetailRepository salesOrderDetailRepository;

    public SalesOrderResponse createSalesOrder(SalesOrderCreationRequest request) {
        SalesOrder salesOrder = salesOrderMapper.toSalesOrder(request);
        SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);
        return salesOrderMapper.toSalesOrderResponse(savedSalesOrder);
    }

    public SalesOrderResponse updateSalesOrder(String orderId, SalesOrderUpdateRequest request) {
        SalesOrder salesOrder = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOT_FOUND));
        salesOrderMapper.updateSalesOrder(salesOrder, request);
        SalesOrder updatedSalesOrder = salesOrderRepository.save(salesOrder);
        return salesOrderMapper.toSalesOrderResponse(updatedSalesOrder);
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
        return salesOrderRepository.findByEmployeeId(employeeId, pageable)
                .map(salesOrderMapper::toSalesOrderResponse);
    }

    public Page<SalesOrderResponse> getSalesOrdersByCustomerId(String customerId, Pageable pageable) {
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
