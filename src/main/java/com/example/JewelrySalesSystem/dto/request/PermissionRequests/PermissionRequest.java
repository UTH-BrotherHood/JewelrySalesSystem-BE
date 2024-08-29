package com.example.JewelrySalesSystem.dto.request.PermissionRequests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {
    @NotBlank(message = "Permission name is required")
    String name;
    String description;
}