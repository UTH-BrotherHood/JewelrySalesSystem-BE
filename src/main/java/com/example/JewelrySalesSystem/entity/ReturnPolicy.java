package com.example.JewelrySalesSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ReturnPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    String returnPolicyId;

    @Column(nullable = false)
    String description; // Description of the return policy

    @Column(nullable = false)
    LocalDateTime effectiveDate; // Date when the policy becomes effective

    @Column(nullable = false)
    LocalDateTime expiryDate;
    // Date when the policy expires
}
