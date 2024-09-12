package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetail, String> {
    List<SalesOrderDetail> findByOrderId(String orderId);

    // Số lượng sản phẩm bán ra
    @Query("SELECT p.productId, p.name, SUM(s.quantity) as totalQuantitySold " +
            "FROM SalesOrderDetail s JOIN Product p ON s.productId = p.productId " +
            "GROUP BY p.productId, p.name")
    List<Object[]> getProductSalesQuantities();



    // Sản phẩm bán chạy nhất
    @Query("SELECT p.productId, p.name, SUM(s.quantity * s.unitPrice) as totalRevenue " +
            "FROM SalesOrderDetail s JOIN Product p ON s.productId = p.productId " +
            "GROUP BY p.productId, p.name " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> getTopSellingProducts();

    @Query("SELECT c.customerId, c.customername, p.productId, p.name as productName, SUM(s.quantity) as totalQuantity " +
            "FROM SalesOrderDetail s " +
            "JOIN SalesOrder o ON s.orderId = o.orderId " +
            "JOIN Customer c ON o.customerId = c.customerId " +
            "JOIN Product p ON s.productId = p.productId " +
            "GROUP BY c.customerId, c.customername, p.productId, p.name")
    List<Object[]> getCustomerPurchaseHistory();

//    // Khách hàng mới vs khách hàng cũ
//    @Query("SELECT " +
//            "COUNT(CASE WHEN createdAt >= :startDate THEN 1 END) as newCustomers, " +
//            "COUNT(CASE WHEN createdAt < :startDate THEN 1 END) as returningCustomers " +
//            "FROM Customer")
//    Object[] getNewVsReturningCustomers(@Param("startDate") LocalDate startDate);
}

