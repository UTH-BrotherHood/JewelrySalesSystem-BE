package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.Category;
import com.example.JewelrySalesSystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>, JpaSpecificationExecutor<Category> {
    boolean existsByCategoryName(String categoryName);
}

