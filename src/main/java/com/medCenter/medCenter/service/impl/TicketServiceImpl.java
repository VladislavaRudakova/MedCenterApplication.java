package com.medCenter.medCenter.service.impl;

import com.medCenter.medCenter.dto.*;
import com.medCenter.medCenter.exception.TicketException;
import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.Ticket;
import com.medCenter.medCenter.model.entity.TicketStates;
import com.medCenter.medCenter.model.repository.TicketRepository;
import com.medCenter.medCenter.service.ClientService;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ServiceService;
import com.medCenter.medCenter.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final PersonalJobService personalJobService;
    private final ClientService clientService;
    private final ServiceService serviceService;


    @Override
    public List<TicketDto> findAll() {
        List<Ticket> tickets = ticketRepository.findAll();
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDto ticketDto = ticketToDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }

    @Override
    public TicketDto findById(Integer id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ticketToDto(ticket);
    }

    public TicketDto ticketToDto(Ticket ticket) {

        ServiceDto serviceDto = serviceService.serviceToDto(ticket.getService());
        TicketDto ticketDto = TicketDto.builder()
                .id(ticket.getId())
                .service(serviceDto)
                .date(ticket.getDate().toLocalDate())
                .time(ticket.getTime().toLocalTime())
                .state(ticket.getState())
                .build();
        if (ticket.getPersonalJob() != null) {
            PersonalJobDto personalJobDto = personalJobService.personalJobToDto(ticket.getPersonalJob());
            ticketDto.setPersonalJob(personalJobDto);
        }
        if (ticket.getClient() != null) {
            ClientDto clientDto = clientService.clientToDto(ticket.getClient());
            ticketDto.setClient(clientDto);
        }
        if (ticket.getSubState() != null) {
            ticketDto.setSubState(ticket.getSubState());
        }
        return ticketDto;
    }


    public Ticket dtoToTicket(TicketDto ticketDto) {
        com.medCenter.medCenter.model.entity.Service service = serviceService.findById(ticketDto.getService().getId());
        Ticket ticket = Ticket.builder()
                .service(service)
                .date(Date.valueOf(ticketDto.getDate()))
                .time(Time.valueOf(ticketDto.getTime()))
                .state(ticketDto.getState())
                .build();

        if (ticketDto.getPersonalJob() != null) {
            PersonalJob personalJob = personalJobService.findById(ticketDto.getPersonalJob().getId());
            ticket.setPersonalJob(personalJob);
        }
        if (ticketDto.getClient() != null) {
            Client client = clientService.findById(ticketDto.getClient().getId());
            ticket.setClient(client);
        }
        if (ticketDto.getSubState() != null) {
            ticket.setSubState(ticketDto.getSubState());
        }

        return ticket;
    }


    //------------------------------------------------------------find----------
    @Override
    public List<PersonalJobDto> findByServiceDateTime(String serviceType, Date date, Time minTime, Time maxTime, String state) {
        List<PersonalJob> personalJobList = ticketRepository.findByServiceDateTime(serviceType, date, minTime, maxTime, state);
        List<PersonalJobDto> personalJobDtoList = new ArrayList<>();
        for (PersonalJob personalJob : personalJobList) {
            PersonalJobDto personalJobDto = personalJobService.personalJobToDto(personalJob);
            personalJobDtoList.add(personalJobDto);
        }
        return personalJobDtoList;
    }

    @Override
    public TicketDto findByDoctorIdAndTime(Integer doctorId, Time time) {
        Ticket ticket = ticketRepository.findByDoctorIdAndTime(doctorId, time);

        return ticketToDto(ticket);
    }

    @Override
    public List<TicketDto> findByPersonalJobIdAndDate(Integer personalJobId, Date date) {
        List<Ticket> tickets = ticketRepository.findByPersonalJobAndDate(personalJobId, date);
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDto ticketDto = ticketToDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;

    }

    @Override
    public List<TicketDto> findByServiceIdAndDate(Integer serviceId, Date date) {
        List<Ticket> tickets = ticketRepository.findByServiceIdAndDate(serviceId, date);
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDto ticketDto = ticketToDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }


    @Override
    public List<TicketDto> findByClient(Integer clientId) {

        List<Ticket> tickets = ticketRepository.findByClientId(clientId);
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDto ticketDto = ticketToDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }

    @Override
    public List<TicketDto> findByPersonalJob(Integer personalJobId) {
        List<Ticket> tickets = ticketRepository.findByPersonalJob(personalJobId);
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDto ticketDto = ticketToDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }

    @Override
    public List<TicketDto> findByPersonalJobAndDatePeriod(Integer personalJobId, Date date1, Date date2) {

        List<Ticket> tickets = ticketRepository.findByPersonalJobAndDatePeriod(personalJobId, date1, date2);
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDto ticketDto = ticketToDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }

    @Override
    public Set<String> findDates(Integer personalJobId) {
        return ticketRepository.findDates(personalJobId);
    }


    @Override
    public List<TicketDto> findActualByPersonalJob(Integer personalJobId) {
        List<Ticket> tickets = ticketRepository.findActualByPersonalJob(personalJobId,TicketStates.NOT_AVAILABLE.toString());
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDto ticketDto = ticketToDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }


    @Override
    public List<TicketDto> findByState(String state) {
        List<Ticket> tickets = ticketRepository.findByState(state);
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDto ticketDto = ticketToDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;

    }

    @Override
    public List<TicketDto> findBySubState(String subState) {
        List<Ticket> tickets = ticketRepository.findBySubState(subState);
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDto ticketDto = ticketToDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }


    //------------------------------------------------------------update/create----------

    @Transactional
    @Override
    public void createTicket(TicketDto ticketDto) {
        Ticket ticket = dtoToTicket(ticketDto);
        ticketRepository.save(ticket);
    }

    @Transactional
    @Override
    public void createTicketsForDay(ScheduleDto scheduleDto, TicketDto ticketDto, Integer timeRange) throws TicketException {

        List<TicketDto> ticketDtoList = new ArrayList<>();
        if (ticketDto.getPersonalJob() != null) {
            ticketDtoList = findByPersonalJobIdAndDate(scheduleDto.getPersonalJob().getId(), Date.valueOf(scheduleDto.getDate()));
        } else {
            ticketDtoList = findByServiceIdAndDate(ticketDto.getService().getId(), Date.valueOf(scheduleDto.getDate()));
        }
        if (!ticketDtoList.isEmpty()) {
            throw new TicketException();
        }
        if (scheduleDto.getDayOff() != null && scheduleDto.getDayOff().equals("dayOff")) {
            throw new TicketException();
        }
        createTickets(ticketDto, scheduleDto, timeRange);

    }


    @Transactional
    @Override
    public void createTicketsForPeriod(List<ScheduleDto> scheduleDtoList, ServiceDto serviceDto, PersonalJobDto personalJobDto, Integer timeRange) throws TicketException {

        Set<String> dates = ticketRepository.findDates(personalJobDto.getId());
        boolean isDate = false;

        for (ScheduleDto scheduleDto : scheduleDtoList) {
            for (String date : dates) {
                if (scheduleDto.getDate().toString().equals(date)) {
                    isDate = true;
                    break;
                }
            }
            if (isDate) continue;

            TicketDto ticketDto = TicketDto.builder()
                    .date(scheduleDto.getDate())
                    .time(scheduleDto.getStartTime())
                    .service(serviceDto)
                    .personalJob(personalJobDto)
                    .state(TicketStates.AVAILABLE.toString())
                    .build();

            createTickets(ticketDto, scheduleDto, timeRange);

        }
    }

    private void createTickets(TicketDto ticketDto, ScheduleDto scheduleDto, Integer timeRange) {
        LocalTime ticketTime = ticketDto.getTime();
        while (ticketDto.getTime().isBefore(scheduleDto.getEndTime())) {
            if ((scheduleDto.getEndTime().minusMinutes(timeRange)).isBefore(ticketTime)) {
                break;
            }
            createTicket(ticketDto);
            ticketTime = ticketTime.plusMinutes(timeRange);
            System.out.println("TICKET TIME!!!" + ticketTime);
            ticketDto.setTime(ticketTime);
        }
    }

    @Transactional
    @Override
    public void updateClientAndState(Integer clientId, String state, Integer ticketId) {
        ticketRepository.updateClientAndState(clientId, state, ticketId);
    }

    @Transactional
    @Override
    public void updateState(String state, Integer ticketId) {
        ticketRepository.updateState(state, ticketId);
    }

    @Transactional
    @Override
    public void updateStateByDate(String state, Date date) {
        ticketRepository.updateStateByDate(state, date);
    }

    @Transactional
    @Override
    public void updateSubState(String subState, Integer ticketId) {
        ticketRepository.updateSubState(subState, ticketId);
    }

    @Transactional
    @Override
    public void makeAvailable(String state, String subState, Integer ticketId) {
        ticketRepository.updateClientToNullStateAndSubState(state, subState, ticketId);
    }


}
