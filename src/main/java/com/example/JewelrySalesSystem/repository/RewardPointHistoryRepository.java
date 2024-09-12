package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.RewardPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardPointHistoryRepository extends JpaRepository<RewardPointHistory, Long> {
    // Các phương thức cần thiết
}
