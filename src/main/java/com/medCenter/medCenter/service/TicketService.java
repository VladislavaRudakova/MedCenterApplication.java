package com.medCenter.medCenter.service;

import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ScheduleDto;
import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.exception.TicketException;
import com.medCenter.medCenter.model.entity.Personal;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.Ticket;
import lombok.Data;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface TicketService {

    List<TicketDto>findAll();

    TicketDto findById(Integer id);

     TicketDto ticketToDto(Ticket ticket);

    Ticket dtoToTicket(TicketDto ticketDto);

    List<TicketDto> findActualByPersonalJob(Integer personalJobId);

    List<TicketDto> findWherePersonalIsNull();

    List<PersonalJobDto> findByServiceDateTime(String serviceType, Date date, Time minTime, Time maxTime, String state);

    TicketDto findByDoctorIdAndTime(Integer doctorId, Time time);

    List<TicketDto> findByPersonalJobIdAndDate(Integer personalJobId, Date date);

    List<TicketDto> findByServiceIdAndDate(Integer serviceId, Date date)throws TicketException;

    List<TicketDto> findByClient(Integer clientId);

    List<TicketDto> findByPersonalJob(Integer personalJobId);

    List<TicketDto> findByPersonalJobAndDatePeriod(Integer personalJobId, Date date1, Date date2);

    Set<String> findDates(Integer personalJobId);

    void updateClientAndState(Integer clientId, String state, Integer ticketId);

    void updateState(String state, Integer ticketId);

    void updateStateByDate(String state, Date date);

    void updateSubState(String subState, Integer ticketId);

    void makeAvailable(String state, String subState, Integer ticketId);

    List<TicketDto> findByState(String state);

    List<TicketDto> findBySubState(String subState);

    void createTicket(TicketDto ticketDto);

    void createTicketsForDay(ScheduleDto scheduleDto, TicketDto ticketDto, Integer timeRange) throws TicketException;

    void createTicketsForPeriod(List<ScheduleDto>scheduleDtoList, ServiceDto serviceDto, PersonalJobDto personalJobDto, Integer timeRange) throws TicketException;


}
