package com.example.JewelrySalesSystem.dto.request.CategoryRequests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateRequest {
    String categoryName;
    String description;
}