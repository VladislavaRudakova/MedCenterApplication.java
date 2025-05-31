package com.medCenter.medCenter.dto;

import com.medCenter.medCenter.model.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDto {
    private Integer id;
    private String type;
    private Double price;
    private String state;
    private List<TicketDto> tickets;
}
