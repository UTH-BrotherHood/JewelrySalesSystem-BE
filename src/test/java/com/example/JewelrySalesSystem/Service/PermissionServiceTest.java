package com.example.JewelrySalesSystem.Service;

import com.example.JewelrySalesSystem.dto.request.PermissionRequests.PermissionRequest;
import com.example.JewelrySalesSystem.dto.response.PermissionResponse;
import com.example.JewelrySalesSystem.entity.Permission;
import com.example.JewelrySalesSystem.mapper.PermissionMapper;
import com.example.JewelrySalesSystem.repository.PermissionRepository;
import com.example.JewelrySalesSystem.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionServiceTest {

  @Mock
  private PermissionRepository permissionRepository;

  @Mock
  private PermissionMapper permissionMapper;

  @InjectMocks
  private PermissionService permissionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreatePermission() {
    PermissionRequest request = new PermissionRequest("READ", "Read permission");
    Permission permission = new Permission();
    permission.setName("READ");
    permission.setDescription("Read permission");

    PermissionResponse response = new PermissionResponse("READ", "Read permission");

    when(permissionMapper.toPermission(request)).thenReturn(permission);
    when(permissionRepository.save(permission)).thenReturn(permission);
    when(permissionMapper.toPermissionResponse(permission)).thenReturn(response);

    PermissionResponse result = permissionService.create(request);

    assertNotNull(result);
    assertEquals("READ", result.getName());
    assertEquals("Read permission", result.getDescription());

    verify(permissionMapper, times(1)).toPermission(request);
    verify(permissionRepository, times(1)).save(permission);
    verify(permissionMapper, times(1)).toPermissionResponse(permission);
  }

  @Test
  void testGetAllPermissions() {
    Permission permission1 = new Permission();
    permission1.setName("READ");
    permission1.setDescription("Read permission");

    Permission permission2 = new Permission();
    permission2.setName("WRITE");
    permission2.setDescription("Write permission");

    List<Permission> permissions = List.of(permission1, permission2);

    PermissionResponse response1 = new PermissionResponse("READ", "Read permission");
    PermissionResponse response2 = new PermissionResponse("WRITE", "Write permission");

    when(permissionRepository.findAll()).thenReturn(permissions);
    when(permissionMapper.toPermissionResponse(permission1)).thenReturn(response1);
    when(permissionMapper.toPermissionResponse(permission2)).thenReturn(response2);

    List<PermissionResponse> result = permissionService.getAll();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("READ", result.get(0).getName());
    assertEquals("WRITE", result.get(1).getName());

    verify(permissionRepository, times(1)).findAll();
    verify(permissionMapper, times(1)).toPermissionResponse(permission1);
    verify(permissionMapper, times(1)).toPermissionResponse(permission2);
  }

  @Test
  void testDeletePermission() {
    String permissionName = "READ";

    doNothing().when(permissionRepository).deleteById(permissionName);

    permissionService.delete(permissionName);

    verify(permissionRepository, times(1)).deleteById(permissionName);
  }
}
