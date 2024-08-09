package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.SalesOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetail, String> {
    List<SalesOrderDetail> findByOrderId(String orderId);
}

