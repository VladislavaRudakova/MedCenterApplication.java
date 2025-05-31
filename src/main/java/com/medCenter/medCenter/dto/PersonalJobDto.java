package com.medCenter.medCenter.dto;

import com.medCenter.medCenter.model.entity.Department;
import com.medCenter.medCenter.model.entity.Personal;
import com.medCenter.medCenter.model.entity.Ticket;
import com.medCenter.medCenter.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalJobDto {

    private Integer id;
    private PersonalDto personal;
    private String jobTitle;
    private DepartmentDto department;
    private UserDto user;
    private String state;
    private List<TicketDto> tickets;

}
