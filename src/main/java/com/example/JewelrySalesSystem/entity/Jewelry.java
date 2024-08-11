package com.example.JewelrySalesSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Jewelry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    String jewelryId;

//    @ManyToOne
//    @JoinColumn(name = "category_id", nullable = false)
//    Category category;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String image;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    BigDecimal costPrice;

    @Column(nullable = false)
    BigDecimal weight;

    @Column(nullable = false)
    BigDecimal laborCost;

    @Column(nullable = false)
    BigDecimal stoneCost;

    @Column(nullable = false)
    Integer stockQuantity;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

}
