package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, String> {
    Page<SalesOrder> findByEmployeeId(String employeeId, Pageable pageable);
    Page<SalesOrder> findByCustomerId(String customerId, Pageable pageable);

    @Query("SELECT COUNT(s) FROM SalesOrder s WHERE s.orderDate = :date")
    Long countByOrderDate(LocalDateTime date);

    @Query("SELECT COUNT(s) FROM SalesOrder s WHERE FUNCTION('MONTH', s.orderDate) = :month AND FUNCTION('YEAR', s.orderDate) = :year")
    Long countByMonth(int month, int year);

    @Query("SELECT SUM(s.originalTotalAmount - s.discountedTotalAmount - s.discountedByRank) FROM SalesOrder s WHERE s.orderDate = :date")
    Double sumTotalAmountByDate(LocalDateTime date);

    @Query("SELECT SUM(s.originalTotalAmount - s.discountedTotalAmount - s.discountedByRank) FROM SalesOrder s WHERE FUNCTION('MONTH', s.orderDate) = :month AND FUNCTION('YEAR', s.orderDate) = :year")
    Double sumTotalAmountByMonth(int month, int year);

    @Query("SELECT p.name, SUM(sd.totalPrice) " +
            "FROM SalesOrderDetail sd " +
            "JOIN Product p ON p.productId = sd.productId " +
            "WHERE sd.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY p.name")
    List<Object[]> getRevenueByProduct(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);



    // Doanh thu theo khách hàng
    @Query("SELECT c.customerId, c.customername, SUM(s.quantity * s.unitPrice) as totalRevenue " +
            "FROM SalesOrderDetail s JOIN SalesOrder o ON s.orderId = o.orderId " +
            "JOIN Customer c ON o.customerId = c.customerId " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY c.customerId, c.customername")
    List<Object[]> getRevenueByCustomer(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


}

