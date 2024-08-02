package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByUsername(String username);

    Optional<Employee> findByUsername(String username);
}
