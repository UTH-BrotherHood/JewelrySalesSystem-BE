package com.example.JewelrySalesSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    String paymentMethodId;

    @Column(nullable = false)
    String paymentMethodName; // Ví dụ: Credit Card, PayPal, Cash, etc.

    @Column(nullable = false)
    String details;

    boolean active;
}
