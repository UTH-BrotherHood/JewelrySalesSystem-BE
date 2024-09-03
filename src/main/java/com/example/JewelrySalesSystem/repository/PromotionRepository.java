package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, String> {
    Optional<Promotion> findByPromotionCode(String promotionCode);
    boolean existsByName(String name);
}
