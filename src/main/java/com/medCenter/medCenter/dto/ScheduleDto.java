package com.medCenter.medCenter.dto;


import com.medCenter.medCenter.model.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {
    private Integer id;
    private ServiceDto service;
    private PersonalJobDto personalJob;
    private LocalDate date;
    private String weekDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private String dayOff;
    private String state;
}
