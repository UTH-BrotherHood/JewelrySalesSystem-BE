package com.example.JewelrySalesSystem.dto.request.CustomerRequests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerUpdateRequest {

    String email;
    String phone;
    String address;
    Integer rewardPoints;

}
