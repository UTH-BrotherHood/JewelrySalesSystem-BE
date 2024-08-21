package com.example.JewelrySalesSystem.dto.request.EmployeeRequests;

import com.example.JewelrySalesSystem.validator.GenericConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeCreationRequest {

    @GenericConstraint(message = "Name must be at least 4 characters long", min = 4)
    String name;

    @NotBlank(message = "Username is required")
    @GenericConstraint(message = "Username must be at least 4 characters long", min = 4)
    String username;

    @NotBlank(message = "Password is required")
    String password;

    @NotBlank(message = "Phone number is required")
    String phoneNumber;
}
