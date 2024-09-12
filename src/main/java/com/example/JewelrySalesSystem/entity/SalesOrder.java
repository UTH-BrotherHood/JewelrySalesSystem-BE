package com.example.JewelrySalesSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    String orderId;

    String customerId;
    String employeeId;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    PaymentMethod paymentMethod;

    String cartId;

    @Column(nullable = false)
    LocalDateTime orderDate;

    @Column(nullable = false)
    BigDecimal originalTotalAmount;

    @Column(nullable = false)
    BigDecimal discountedTotalAmount;

    @Column(nullable = false)
    BigDecimal discountedByRank;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "return_policy_id")
    ReturnPolicy returnPolicy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;


}

