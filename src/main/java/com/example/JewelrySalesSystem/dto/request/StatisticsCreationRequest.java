package com.example.JewelrySalesSystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsCreationRequest {
    @NotBlank(message = "REPORT_TYPE_REQUIRED")
    String reportType;

    @NotNull(message = "REPORT_DATE_REQUIRED")
    LocalDateTime reportDate;

    @NotBlank(message = "DATA_REQUIRED")
    String data;
}


