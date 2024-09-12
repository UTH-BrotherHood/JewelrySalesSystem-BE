package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReturnPolicyResponse {
    String returnPolicyId;
    String description;
    LocalDateTime effectiveDate;
    LocalDateTime expiryDate;
}
