package com.example.JewelrySalesSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class BuyBackOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  int buyBackId;
  int saleId;
  int customerId;
  int employeeId;
  LocalDateTime buyBackDate;
  BigDecimal totalAmount;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  LocalDateTime createAt;
  @UpdateTimestamp
  @Column(nullable = false)
  LocalDateTime updateAt;
}
