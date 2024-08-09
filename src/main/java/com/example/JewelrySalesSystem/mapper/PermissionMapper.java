package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.PermissionRequests.PermissionRequest;
import com.example.JewelrySalesSystem.dto.response.PermissionResponse;
import com.example.JewelrySalesSystem.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}