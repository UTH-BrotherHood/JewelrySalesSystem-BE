package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.ReturnPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnPolicyRepository extends JpaRepository<ReturnPolicy, String> {
}
