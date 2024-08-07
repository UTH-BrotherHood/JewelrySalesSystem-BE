package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.RoleRequest;
import com.example.JewelrySalesSystem.dto.response.RoleResponse;
import com.example.JewelrySalesSystem.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}