package com.example.JewelrySalesSystem.repository;

import com.example.JewelrySalesSystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    boolean existsByUsername(String username);

    Optional<Employee> findByUsername(String username);

    @Query("SELECT e.employeeId, e.name, SUM(s.discountedTotalAmount) as totalRevenue " +
            "FROM SalesOrder s JOIN Employee e ON s.employeeId = e.employeeId " +
            "WHERE s.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY e.employeeId, e.name")
    List<Object[]> getRevenueByEmployee(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
