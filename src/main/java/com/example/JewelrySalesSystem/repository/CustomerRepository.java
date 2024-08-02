package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  CustomerRepository extends JpaRepository<Customer, String> {
    boolean existsBycustomername(String customername);

    Optional<Customer> findBycustomername(String customername);
}

