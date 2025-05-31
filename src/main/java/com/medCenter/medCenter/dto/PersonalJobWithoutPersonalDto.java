package com.medCenter.medCenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalJobWithoutPersonalDto {

    private Integer id;
    private String name;
    private String surname;
    private Date birthdate;
    private Date employmentDate;
    private Date dismissalDate;
    private Integer experience;
    private String jobTitle;
    private String departmentName;
    private String state;


}
