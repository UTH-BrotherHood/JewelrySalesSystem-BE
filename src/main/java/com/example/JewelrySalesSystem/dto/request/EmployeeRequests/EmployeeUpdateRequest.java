package com.example.JewelrySalesSystem.dto.request.EmployeeRequests;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeUpdateRequest {
    String name;
    String username;
    String phoneNumber;
    List<String> roles;
}
