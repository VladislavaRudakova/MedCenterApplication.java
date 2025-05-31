package com.medCenter.medCenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalDto {
    private Integer id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private LocalDate employmentDate;
    private LocalDate dismissalDate;
    private Integer experience;
    private String state;
    private String photo;
}
