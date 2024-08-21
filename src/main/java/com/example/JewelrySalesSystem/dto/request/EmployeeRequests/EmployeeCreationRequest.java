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

    @GenericConstraint(message = "Invalid name ( đã được custom )", min = 4)
    String name;

    @NotBlank(message = "USERNAME_INVALID")
    @GenericConstraint(message = "Invalid username ( đã được custom )" , min = 4)
    String username;

    @NotBlank(message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "PHONE_NUMBER_INVALID")
    String phoneNumber;
}
