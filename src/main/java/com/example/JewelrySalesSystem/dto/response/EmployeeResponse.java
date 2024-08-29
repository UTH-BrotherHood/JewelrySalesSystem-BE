package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeResponse {
    String employeeId;
    String name;
    String username;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Set<RoleResponse> roles;
}
