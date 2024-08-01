package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {
    String id;
    String customername;
    String email;
    String phone;
    String address;
    Integer rewardPoints;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
