package com.medCenter.medCenter.dto;
import com.medCenter.medCenter.model.entity.MedCard;
import com.medCenter.medCenter.model.entity.Ticket;
import com.medCenter.medCenter.model.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {

    private Integer id;
    private String name;
    private String surname;
    private String telephoneNumber;
    private UserDto user;
    private String state;
    private List<TicketDto> tickets;

}
