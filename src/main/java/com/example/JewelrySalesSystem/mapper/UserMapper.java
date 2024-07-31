package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.UserCreationRequest;
import com.example.JewelrySalesSystem.dto.request.UserUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.UserResponse;
import com.example.JewelrySalesSystem.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
