package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    boolean existsByName(String name);

    // Tồn kho sản phẩm
    @Query("SELECT p.productId, p.name, p.stockQuantity " +
            "FROM Product p")
    List<Object[]> getProductStockLevels();


    // Sản phẩm tồn kho lâu nhất
    @Query("SELECT p.productId, p.name, p.stockQuantity, p.createdAt " +
            "FROM Product p " +
            "ORDER BY p.createdAt ASC")
    List<Object[]> getOldestStockProducts();

}
