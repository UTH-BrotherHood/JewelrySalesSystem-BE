package com.example.JewelrySalesSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    String itemId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    Cart cart;


    @Column(nullable = false)
    String productId;

    @Column(nullable = false)
    Integer quantity;

    @Column(nullable = false)
    BigDecimal price;
}
