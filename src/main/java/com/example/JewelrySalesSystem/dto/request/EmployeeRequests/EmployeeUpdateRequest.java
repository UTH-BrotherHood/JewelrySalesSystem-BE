package com.example.JewelrySalesSystem.dto.request.EmployeeRequests;

import com.example.JewelrySalesSystem.validator.GenericConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeUpdateRequest {
    @GenericConstraint(message = "Name must be at least 4 characters long", min = 4)
    String name;

    @GenericConstraint(message = "Username must be at least 4 characters long", min = 4)
    String username;

    @GenericConstraint(message = "Invalid phone number format", pattern = "^\\+?[0-9. ()-]{7,25}$")
    String phoneNumber;

    List<String> roles;
}

