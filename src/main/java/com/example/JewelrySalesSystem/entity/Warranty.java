package com.example.JewelrySalesSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Warranty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String warrantyId;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    Product product;

    @Column(nullable = false)
    LocalDateTime warrantyStartDate;

    @Column(nullable = false)
    LocalDateTime warrantyEndDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;
}
