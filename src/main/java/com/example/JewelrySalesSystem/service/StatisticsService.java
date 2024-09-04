package com.example.JewelrySalesSystem.service;
import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsCreationRequest;
import com.example.JewelrySalesSystem.dto.request.StatisticsRequests.StatisticsUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.StatisticsResponse;
import com.example.JewelrySalesSystem.entity.SalesOrder;
import com.example.JewelrySalesSystem.entity.Statistics;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.StatisticsMapper;
import com.example.JewelrySalesSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final StatisticsMapper statisticsMapper;
    private final SalesOrderRepository salesOrderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final PromotionRepository promotionRepository;
    private final SalesOrderDetailRepository salesOrderDetailRepository;
    private final EmployeeRepository employeeRepository;

    public StatisticsResponse createStatistics(StatisticsCreationRequest request) {
        Statistics statistics = statisticsMapper.toStatistics(request);
        Statistics savedStatistics = statisticsRepository.save(statistics);
        return statisticsMapper.toStatisticsResponse(savedStatistics);
    }

    public StatisticsResponse updateStatistics(String statisticId, StatisticsUpdateRequest request) {
        Statistics statistics = statisticsRepository.findById(statisticId)
                .orElseThrow(() -> new AppException(ErrorCode.STATISTICS_NOT_FOUND));
        statisticsMapper.updateStatistics(statistics, request);
        Statistics updatedStatistics = statisticsRepository.save(statistics);
        return statisticsMapper.toStatisticsResponse(updatedStatistics);
    }

    public void deleteStatistics(String statisticId) {
        if (!statisticsRepository.existsById(statisticId)) {
            throw new AppException(ErrorCode.STATISTICS_NOT_FOUND);
        }
        statisticsRepository.deleteById(statisticId);
    }

    public StatisticsResponse getStatistics(String statisticId) {
        Statistics statistics = statisticsRepository.findById(statisticId)
                .orElseThrow(() -> new AppException(ErrorCode.STATISTICS_NOT_FOUND));
        return statisticsMapper.toStatisticsResponse(statistics);
    }

    public Page<StatisticsResponse> getStatistics(Pageable pageable) {
        return statisticsRepository.findAll(pageable)
                .map(statisticsMapper::toStatisticsResponse);
    }

    public Long getTotalOrdersByDate(LocalDateTime date) {
        return salesOrderRepository.countByOrderDate(date);
    }

    public Long getTotalOrdersByMonth(int month, int year) {
        return salesOrderRepository.countByMonth(month, year);
    }

    public Double getTotalSalesAmountByDate(LocalDateTime date) {
        return salesOrderRepository.sumTotalAmountByDate(date);
    }

    public Double getTotalSalesAmountByMonth(int month, int year) {
        return salesOrderRepository.sumTotalAmountByMonth(month, year);
    }
    public List<Map<String, Object>> getRevenueByProduct(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = salesOrderRepository.getRevenueByProduct(startDate, endDate);
        List<Map<String, Object>> revenueList = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> revenue = new HashMap<>();
            revenue.put("productName", result[0]);
            revenue.put("totalRevenue", result[1]);
            revenueList.add(revenue);
        }

        return revenueList;
    }



    public List<Map<String, Object>> getProductSalesQuantities() {
        List<Object[]> results = salesOrderDetailRepository.getProductSalesQuantities();
        List<Map<String, Object>> salesQuantities = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> sales = new HashMap<>();
            sales.put("productId", result[0]);
            sales.put("productName", result[1]);
            sales.put("totalQuantitySold", result[2]);
            salesQuantities.add(sales);
        }

        return salesQuantities;
    }

    public List<Map<String, Object>> getProductStockLevels() {
        List<Object[]> results = productRepository.getProductStockLevels();
        List<Map<String, Object>> stockLevels = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> stock = new HashMap<>();
            stock.put("productId", result[0]);
            stock.put("productName", result[1]);
            stock.put("stockQuantity", result[2]);
            stockLevels.add(stock);
        }

        return stockLevels;
    }

    public List<Map<String, Object>> getTopSellingProducts() {
        List<Object[]> results = salesOrderDetailRepository.getTopSellingProducts();
        List<Map<String, Object>> topProducts = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> product = new HashMap<>();
            product.put("productId", result[0]);
            product.put("productName", result[1]);
            product.put("totalRevenue", result[2]);
            topProducts.add(product);
        }

        return topProducts;
    }

    public List<Map<String, Object>> getOldestStockProducts() {
        List<Object[]> results = productRepository.getOldestStockProducts();
        List<Map<String, Object>> oldestProducts = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> product = new HashMap<>();
            product.put("productId", result[0]);
            product.put("productName", result[1]);
            product.put("stockQuantity", result[2]);
            product.put("createdAt", result[3]);
            oldestProducts.add(product);
        }

        return oldestProducts;
    }




    public List<Map<String, Object>> getRevenueByCustomer(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = salesOrderRepository.getRevenueByCustomer(startDate, endDate);
        List<Map<String, Object>> revenueList = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> revenue = new HashMap<>();
            revenue.put("customerId", result[0]);
            revenue.put("totalRevenue", result[1]);
            revenueList.add(revenue);
        }

        return revenueList;
    }

    public List<Map<String, Object>> getCustomerPurchaseHistory() {
        List<Object[]> results = salesOrderDetailRepository.getCustomerPurchaseHistory();
        List<Map<String, Object>> purchaseHistory = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> history = new HashMap<>();
            history.put("customerId", result[0]);
            history.put("customerName", result[1]);
            history.put("productId", result[2]);
            history.put("productName", result[3]);
            history.put("totalQuantity", result[4]);
            purchaseHistory.add(history);
        }

        return purchaseHistory;
    }

    public List<Map<String, Object>> getRevenueByEmployee(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = employeeRepository.getRevenueByEmployee(startDate, endDate);
        List<Map<String, Object>> revenueList = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> revenue = new HashMap<>();
            revenue.put("employeeId", result[0]);
            revenue.put("employeeName", result[1]);
            revenue.put("totalRevenue", ((BigDecimal) result[2]).doubleValue());
            revenueList.add(revenue);
        }

        return revenueList;
    }

}
