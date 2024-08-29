package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.BuyBackOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyBackOrderRepository extends JpaRepository<BuyBackOrder, Integer> {

}
