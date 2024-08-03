package com.example.JewelrySalesSystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsResponse {
    Integer statisticId;
    String reportType;
    LocalDateTime reportDate;
    String data;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
