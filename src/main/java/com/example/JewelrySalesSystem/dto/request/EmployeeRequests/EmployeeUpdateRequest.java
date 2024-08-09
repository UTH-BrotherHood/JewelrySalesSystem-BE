package com.example.JewelrySalesSystem.dto.request.EmployeeRequests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeUpdateRequest {
    String name;
    String username;
    String password; // Chỉ cần nếu muốn cập nhật mật khẩu
    String phoneNumber;
}
