package com.example.JewelrySalesSystem.Service;

import com.example.JewelrySalesSystem.dto.request.RoleRequests.RoleRequest;
import com.example.JewelrySalesSystem.dto.response.RoleResponse;
import com.example.JewelrySalesSystem.entity.Permission;
import com.example.JewelrySalesSystem.entity.Role;
import com.example.JewelrySalesSystem.mapper.RoleMapper;
import com.example.JewelrySalesSystem.repository.PermissionRepository;
import com.example.JewelrySalesSystem.repository.RoleRepository;
import com.example.JewelrySalesSystem.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

  @InjectMocks
  private RoleService roleService;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private PermissionRepository permissionRepository;

  @Mock
  private RoleMapper roleMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateRole() {
    RoleRequest request = new RoleRequest();
    Set<String> permissionsSet = new HashSet<>(Arrays.asList("perm1", "perm2"));
    request.setPermissions(permissionsSet);

    Role role = new Role();
    RoleResponse roleResponse = new RoleResponse();

    // Tạo các đối tượng Permission giả định
    Permission perm1 = new Permission(); // Đảm bảo rằng các đối tượng này được cấu hình đúng
    Permission perm2 = new Permission();
    Set<Permission> permissionsResult = new HashSet<>(Arrays.asList(perm1, perm2));

    // Khi gọi roleMapper.toRole(request) thì trả về role
    when(roleMapper.toRole(request)).thenReturn(role);

    // Khi gọi permissionRepository.findAllById(permissionsSet) thì trả về permissionsResult
    when(permissionRepository.findAllById(permissionsSet)).thenReturn(permissionsResult.stream().collect(Collectors.toList()));

    // Khi gọi roleRepository.save(role) thì trả về role
    when(roleRepository.save(role)).thenReturn(role);

    // Khi gọi roleMapper.toRoleResponse(role) thì trả về roleResponse
    when(roleMapper.toRoleResponse(role)).thenReturn(roleResponse);

    RoleResponse result = roleService.create(request);

    assertEquals(roleResponse, result);
    verify(roleMapper).toRole(request);
    verify(permissionRepository).findAllById(permissionsSet);
    verify(roleRepository).save(role);
    verify(roleMapper).toRoleResponse(role);
  }

  @Test
  public void testGetAllRoles() {
    Role role = new Role();
    RoleResponse roleResponse = new RoleResponse();

    when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));
    when(roleMapper.toRoleResponse(role)).thenReturn(roleResponse);

    List<RoleResponse> result = roleService.getAll();

    assertEquals(Collections.singletonList(roleResponse), result);
    verify(roleRepository).findAll();
    verify(roleMapper).toRoleResponse(role);
  }

  @Test
  public void testDeleteRole() {
    String roleId = "roleId";

    doNothing().when(roleRepository).deleteById(roleId);

    roleService.delete(roleId);

    verify(roleRepository).deleteById(roleId);
  }
}
