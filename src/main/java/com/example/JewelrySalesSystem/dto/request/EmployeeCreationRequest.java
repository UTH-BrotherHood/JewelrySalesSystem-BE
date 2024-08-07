package com.example.JewelrySalesSystem.dto.request;

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
    @NotBlank(message = "NAME_INVALID")
    String name;

    @NotBlank(message = "USERNAME_INVALID")
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @NotBlank(message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "PHONE_NUMBER_INVALID")
    String phoneNumber;
}
