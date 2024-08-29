package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.Product;
import com.example.JewelrySalesSystem.entity.Warranty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WarrantyRepository extends JpaRepository<Warranty, String> {
    List<Warranty> findByProduct(Product product);
}
