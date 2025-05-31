package com.medCenter.medCenter.service.impl;

import com.medCenter.medCenter.dto.DepartmentDto;
import com.medCenter.medCenter.model.entity.Department;
import com.medCenter.medCenter.model.repository.DepartmentRepository;
import com.medCenter.medCenter.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDto> findAll() {
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDto> departmentDtoList = new ArrayList<>();

        for (Department department : departments) {
            DepartmentDto departmentDto = departmentToDto(department);
            departmentDtoList.add(departmentDto);
        }

        return departmentDtoList;
    }

    @Override
    public DepartmentDto departmentToDto(Department department) {
        DepartmentDto departmentDto = DepartmentDto.builder()
                .name(department.getName())
                .build();
        if (department.getDepartment() != null) {
            DepartmentDto departmentDto1 = DepartmentDto.builder()
                    .id(department.getDepartment().getId()).build();
            departmentDto.setDepartment(departmentDto1);
        }
        return departmentDto;
    }

    @Override
    public Department dtoToDepartment(DepartmentDto departmentDto) {
        Department department = Department.builder()
                .name(departmentDto.getName()).build();
        if (departmentDto.getDepartment() != null) {
            Department department1 = Department.builder()
                    .id(departmentDto.getDepartment().getId()).build();
            department.setDepartment(department1);
        }
        return department;
    }

    @Transactional
    @Override
    public void createDepartment(DepartmentDto departmentDto) {
        Department department = dtoToDepartment(departmentDto);
        departmentRepository.save(department);
    }

    @Override
    public DepartmentDto findByName(String name) {
        Department department = departmentRepository.findByName(name);

        return departmentToDto(department);
    }
}
