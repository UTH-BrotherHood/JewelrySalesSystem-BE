package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.request.EmployeeCreationRequest;
import com.example.JewelrySalesSystem.dto.request.EmployeeUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.EmployeeResponse;
import com.example.JewelrySalesSystem.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface EmployeeMapper {
    Employee toEmployee(EmployeeCreationRequest request);

    EmployeeResponse toEmployeeResponse(Employee employee);

    void updateEmployee(@MappingTarget Employee employee, EmployeeUpdateRequest request);
}
