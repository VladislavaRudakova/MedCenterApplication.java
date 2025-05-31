package com.medCenter.medCenter;

import com.medCenter.medCenter.dto.DepartmentDto;
import com.medCenter.medCenter.service.DepartmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DepartmentServiceTest {
    @Autowired
    private DepartmentService departmentService;


    public void findByName() {


    }


    @Test
    public void createDepartment() {

        Assertions.assertNotNull(departmentService);

        DepartmentDto departmentDto = DepartmentDto.builder()
                .name("testDepartment").build();

        departmentService.createDepartment(departmentDto);
        DepartmentDto departmentDtoSaved = departmentService.findByName("testDepartment");

        Assertions.assertNotNull(departmentDtoSaved);
        Assertions.assertEquals(departmentDtoSaved.getName(),"testDepartment");

    }







}
