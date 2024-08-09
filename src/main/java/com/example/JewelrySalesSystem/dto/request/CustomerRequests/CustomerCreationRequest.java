package com.example.JewelrySalesSystem.dto.request.CustomerRequests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCreationRequest {
    @NotBlank(message = "USERNAME_INVALID")
    @Size(min = 4, message = "USERNAME_INVALID")
    String customername;

    @Email(message = "EMAIL_INVALID")
    @NotBlank(message = "EMAIL_INVALID")
    String email;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "PHONE_INVALID")
    String phone;

    @NotBlank(message = "ADDRESS_INVALID")
    String address;

    Integer rewardPoints;
}

