package com.example.JewelrySalesSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String statisticId;

    @Column(nullable = false)
    String reportType;

    @Column(nullable = false)
    LocalDateTime reportDate;

    @Column(nullable = false)
    String data;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;
}
