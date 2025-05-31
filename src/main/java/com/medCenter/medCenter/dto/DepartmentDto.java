package com.medCenter.medCenter.dto;

import com.medCenter.medCenter.model.entity.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDto {
    private Integer id;
    private  String name;
    private DepartmentDto department;
    private String state;
}
