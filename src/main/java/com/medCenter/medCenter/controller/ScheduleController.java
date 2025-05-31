package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.*;
import com.medCenter.medCenter.exception.ScheduleExistException;
import com.medCenter.medCenter.exception.ScheduleNotFoundException;
import com.medCenter.medCenter.model.entity.ScheduleStates;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ScheduleService;
import com.medCenter.medCenter.service.ServiceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin/")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final PersonalJobService personalJobService;
    private final ServiceService serviceService;

    @PostMapping(value = "/findSchedule")
    public String findSchedule(@RequestParam String personalJobId, HttpSession session, Model model) {

        PersonalJobDto personalJobDto = personalJobService.findByIdDto(Integer.valueOf(personalJobId));
        List<String> times = generateTime();
        session.setAttribute("personalJob", personalJobDto);
        model.addAttribute("personalJob", personalJobDto);
        model.addAttribute("times", times);

        List<ScheduleDto> scheduleList = new ArrayList<>();
        try {
            scheduleList = scheduleService.findByPersonalId(personalJobId);
        } catch (ScheduleNotFoundException e) {
            return "adminPersonalSchedulePage";
        }
        List<TicketDto> ticketDtoList = scheduleList.getFirst().getPersonalJob().getTickets();
        Set<String> serviceTypes = serviceService.findAllTypes();

        Map<LocalDate, List<TicketDto>> ticketsByDate = new HashMap<>();
        for (ScheduleDto schedule : scheduleList) {
            List<TicketDto> filteredTickets = ticketDtoList.stream()
                    .filter(ticket -> ticket.getDate().equals(schedule.getDate()))
                    .collect(Collectors.toList());
            ticketsByDate.put(schedule.getDate(), filteredTickets);
        }
        model.addAttribute("ticketsByDate", ticketsByDate);
        model.addAttribute("scheduleList", scheduleList);
        model.addAttribute("dayTickets", ticketDtoList);
        model.addAttribute("serviceTypes", serviceTypes);
        return "adminPersonalSchedulePage";
    }

    @PostMapping(value = "/findScheduleForService")
    public String findScheduleForService(@RequestParam String serviceId, HttpSession session, Model model) {

        ServiceDto serviceDto = serviceService.findByIdDto(Integer.valueOf(serviceId));
        List<String> times = generateTime();

        session.setAttribute("service", serviceDto);
        model.addAttribute("service", serviceDto);
        model.addAttribute("times", times);

        List<ScheduleDto> scheduleList = new ArrayList<>();
        try {
            scheduleList = scheduleService.findByServiceIdAndState(Integer.valueOf(serviceId), ScheduleStates.OPEN.toString());
        } catch (ScheduleNotFoundException e) {
            return "adminPersonalSchedulePage";
        }
        List<TicketDto> ticketDtoList = scheduleList.getFirst().getService().getTickets();
        Set<String> serviceTypes = serviceService.findAllTypes();

        Map<LocalDate, List<TicketDto>> ticketsByDate = new HashMap<>();
        for (ScheduleDto schedule : scheduleList) {
            List<TicketDto> filteredTickets = ticketDtoList.stream()
                    .filter(ticket -> ticket.getDate().equals(schedule.getDate()))
                    .collect(Collectors.toList());
            ticketsByDate.put(schedule.getDate(), filteredTickets);
        }
        model.addAttribute("ticketsByDate", ticketsByDate);
        model.addAttribute("scheduleList", scheduleList);
        model.addAttribute("dayTickets", ticketDtoList);
        model.addAttribute("serviceTypes", serviceTypes);
        return "adminPersonalSchedulePage";
    }


    @PostMapping(value = "/setScheduleForDay")
    public String setScheduleForDay(Model model, HttpSession session, @RequestParam String date1, @RequestParam String startTime1, @RequestParam String endTime1, @RequestParam String personalJobId) {

        PersonalJobDto personalJobDto = PersonalJobDto.builder()
                .id(Integer.valueOf(personalJobId))
                .build();

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(Date.valueOf(date1).toLocalDate())
                .startTime(Time.valueOf(startTime1 + ":00").toLocalTime())
                .endTime(Time.valueOf(endTime1 + ":00").toLocalTime())
                .personalJob(personalJobDto)
                .state(ScheduleStates.OPEN.toString())
                .build();
        try {
            scheduleService.setScheduleForDay(scheduleDto);
        } catch (ScheduleExistException e) {
            e.getLocalizedMessage();
        }
        return findSchedule(personalJobId, session, model);
    }


    @PostMapping(value = "/setServiceScheduleForDay")
    public String setServiceScheduleForDay(Model model, HttpSession session, @RequestParam String date1, @RequestParam String startTime1, @RequestParam String endTime1) {

        ServiceDto serviceDto = (ServiceDto) session.getAttribute("service");

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(Date.valueOf(date1).toLocalDate())
                .service(serviceDto)
                .startTime(Time.valueOf(startTime1 + ":00").toLocalTime())
                .endTime(Time.valueOf(endTime1 + ":00").toLocalTime())
                .state(ScheduleStates.OPEN.toString())
                .build();
        try {
            scheduleService.setScheduleForDay(scheduleDto);
        } catch (ScheduleExistException e) {
            e.getLocalizedMessage();
        }
        return findScheduleForService(serviceDto.getId().toString(), session, model);
    }


    @PostMapping(value = "/setScheduleForPeriod")
    public String setFixedScheduleForPeriod(Model model, HttpSession session, @RequestParam String date1,
                                            @RequestParam String date2, @RequestParam String startTime1, @RequestParam String endTime1, @RequestParam String personalJobId) {

        PersonalJobDto personalJobDto = (PersonalJobDto)session.getAttribute("personalJob");

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(Date.valueOf(date1).toLocalDate())
                .startTime(Time.valueOf(startTime1+":00").toLocalTime())
                .endTime(Time.valueOf(endTime1+":00").toLocalTime())
                .state(ScheduleStates.OPEN.toString())
                .personalJob(personalJobDto)
                .build();

        scheduleService.setScheduleWithFixedTimeForPeriod(date2, scheduleDto);
        return findSchedule(personalJobId, session, model);
    }

    @PostMapping(value = "/setServiceScheduleForPeriod")
    public String setServiceFixedScheduleForPeriod(Model model, HttpSession session, @RequestParam String date1,
                                            @RequestParam String date2, @RequestParam String startTime1, @RequestParam String endTime1) {

        ServiceDto serviceDto = (ServiceDto) session.getAttribute("service");
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(Date.valueOf(date1).toLocalDate())
                .startTime(Time.valueOf(startTime1+":00").toLocalTime())
                .endTime(Time.valueOf(endTime1+":00").toLocalTime())
                .service(serviceDto)
                .state("open")
                .build();

        scheduleService.setScheduleWithFixedTimeForPeriod(date2, scheduleDto);

        return findScheduleForService(serviceDto.getId().toString(), session, model);
    }




    @PostMapping(value = "/correctSchedule")
    public String correctSchedule(Model model, HttpSession session, @RequestParam String instance,
                                  @RequestParam Integer scheduleId, @RequestParam String startTime, @RequestParam String endTime,
                                  @RequestParam String startTimeExist, @RequestParam String endTimeExist) {
        startTime = !startTime.isEmpty() ? startTime + ":00" : startTimeExist + ":00";
        endTime = !endTime.isEmpty() ? endTime + ":00" : endTimeExist + ":00";
        scheduleService.updateSchedule(Time.valueOf(startTime), Time.valueOf(endTime), scheduleId);

        if (instance.equals("service")){
            ServiceDto serviceDto = (ServiceDto) session.getAttribute("service");
            return findScheduleForService(serviceDto.getId().toString(), session, model);
        } else {
            PersonalJobDto personalJobDto = (PersonalJobDto)session.getAttribute("personalJob");
            return findSchedule(personalJobDto.getId().toString(), session, model);
        }
    }


    @PostMapping(value = "/setDayOff")
    public String setDayOff(Model model, HttpSession session, @RequestParam Integer scheduleId, @RequestParam Integer personalJobId) {
        scheduleService.setDayOff("day off", scheduleId);
        return findSchedule(personalJobId.toString(), session, model);
    }


    private List<String> generateTime() {
        List<String> times = new ArrayList<>();
        LocalTime time = LocalTime.of(8, 0);
        times.add(time.toString());
        Time time1 = null;
        for (int i = 0; i < 12; i++) {
            time = time.plusHours(1);
            times.add(time.toString());
        }
        System.out.println(times);
        return times;
    }
}
