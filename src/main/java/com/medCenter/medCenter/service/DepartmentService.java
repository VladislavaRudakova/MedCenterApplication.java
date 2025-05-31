package com.medCenter.medCenter.service;

import com.medCenter.medCenter.dto.DepartmentDto;
import com.medCenter.medCenter.model.entity.Department;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDto> findAll();

    DepartmentDto departmentToDto(Department department);

    Department dtoToDepartment(DepartmentDto departmentDto);

    void createDepartment(DepartmentDto departmentDto);

    DepartmentDto findByName(String name);


}
