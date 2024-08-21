package com.example.JewelrySalesSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String customerId;

    String customername;
    String email;
    String phone;
    String address;
    Integer rewardPoints;

    @ManyToMany
    Set<Role> roles;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<SalesOrder> salesOrders;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;
}
