package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Customer, String> {
    boolean existsByUsername(String username);

    Optional<Customer> findByUsername(String username);
}
