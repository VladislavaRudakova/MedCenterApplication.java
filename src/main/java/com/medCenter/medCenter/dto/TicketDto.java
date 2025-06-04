package com.medCenter.medCenter.dto;

import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {
    private Integer id;
    private ServiceDto service;
    private ClientDto client;
    private PersonalJobDto personalJob;
    private LocalDate date;
    private LocalTime time;
    private String state;
    private String subState;
    private String cancelRequestFromRole;
}
