package com.medCenter.medCenter.dto;

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
public class TicketDtoLight {
    private Integer id;
    private Integer serviceId;
    private Integer clientId;
    private Integer personalJobId;
    private LocalDate date;
    private LocalTime time;
    private String state;




}
