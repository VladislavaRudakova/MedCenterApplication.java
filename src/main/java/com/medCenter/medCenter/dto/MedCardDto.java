package com.medCenter.medCenter.dto;

import com.medCenter.medCenter.model.entity.PersonalJob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedCardDto {
    private Integer id;
    private ClientDto client;
    private PersonalJobDto personalJob;
    private ServiceDto service;
    private LocalDate date;
    private String complaints;
    private String diagnosis;
    private String examinationResult;
    private String appointment;
    private String state;
}
