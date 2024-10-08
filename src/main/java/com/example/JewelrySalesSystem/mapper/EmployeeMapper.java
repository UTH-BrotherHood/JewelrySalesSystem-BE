package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.EmployeeRequests.EmployeeCreationRequest;
import com.example.JewelrySalesSystem.dto.request.EmployeeRequests.EmployeeUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.EmployeeResponse;
import com.example.JewelrySalesSystem.entity.Employee;
import com.example.JewelrySalesSystem.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface EmployeeMapper {
    Employee toEmployee(EmployeeCreationRequest request);

    EmployeeResponse toEmployeeResponse(Employee employee);

    @Mapping(target = "roles", ignore = true)
    void updateEmployee(@MappingTarget Employee employee, EmployeeUpdateRequest request);


}
