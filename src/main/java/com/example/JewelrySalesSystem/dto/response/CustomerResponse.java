package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {
    String customerId;
    String customername;
    String email;
    String phone;
    String address;
    Integer rewardPoints;
    Set<RoleResponse> roles;
    String rankLevel;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
