package com.example.JewelrySalesSystem.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsUpdateRequest {
    String reportType;
    LocalDateTime reportDate;
    String data;
}