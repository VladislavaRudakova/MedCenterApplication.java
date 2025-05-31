package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ScheduleDto;
import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.exception.TicketException;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.TicketStates;
import com.medCenter.medCenter.model.entity.TicketSubStates;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ScheduleService;
import com.medCenter.medCenter.service.ServiceService;
import com.medCenter.medCenter.service.TicketService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/")
@PreAuthorize("isAuthenticated()")
public class AdminTicketController {

    private final TicketService ticketService;
    private final ServiceService serviceService;
    private final ScheduleService scheduleService;
    private final AdminStartController adminStartController;

    @GetMapping(value = "/ticketOperations")
    public String findAllPersonal(Model model) {

        List<ServiceDto> services = serviceService.findAll();
        model.addAttribute("services", services);
        return "adminFindTicketsPage";
    }

    @PostMapping(value = "/ticketsForCancellation")
    public String showTicketsForCancellation(Model model, HttpSession session, @AuthenticationPrincipal UserDetailsImpl user) {
        List<TicketDto> tickets = ticketService.findBySubState(TicketSubStates.REQUEST_FOR_CANCELLATION.toString());
        if (tickets.isEmpty()) {
            return adminStartController.start(model, session, user);
        }
        model.addAttribute("ticketsForCancellation", tickets);
        return "ticketsForCancellationPage";
    }

    @PostMapping(value = "/cancelTicket")
    public String approveCancellationRequest(@RequestParam String ticketId, Model model, HttpSession httpSession, @AuthenticationPrincipal UserDetailsImpl user) {
        ticketService.makeAvailable(TicketStates.AVAILABLE.toString(), TicketSubStates.CANCELLED.toString(), Integer.valueOf(ticketId));
        return showTicketsForCancellation(model, httpSession, user);
    }

    @PostMapping(value = "/editTicket")
    public String editTicketState(@RequestParam String ticketId, Model model) {

        return "";
    }


    @PostMapping(value = "/findAllTickets")
    public String findAllTickets(Model model) {
        List<TicketDto> ticketDtoList = ticketService.findAll();
        model.addAttribute("tickets", ticketDtoList);
        return "adminFoundTicketsPage";
    }


    @PostMapping(value = "/findTickets")
    public String findTickets() {
        List<TicketDto> ticketDtoList = ticketService.findAll();
        return "adminFoundTicketsPage";
    }

    @PostMapping(value = "/findDayTickets")
    public String findDayTickets(Model model, @RequestParam String personalJobId, @RequestParam String date) {
        List<TicketDto> ticketDtoList = ticketService.findByPersonalJobIdAndDate(Integer.valueOf(personalJobId), Date.valueOf(date));
        model.addAttribute("tickets", ticketDtoList);
        return "adminFoundTicketsPage";
    }


    @PostMapping(value = "/findClientTickets")
    public String findClientTickets(@RequestParam String clientId, Model model) {
        List<TicketDto> ticketDtoList = ticketService.findByClient(Integer.valueOf(clientId));
        model.addAttribute("tickets", ticketDtoList);
        return "adminFoundTicketsPage";
    }

    @PostMapping(value = "/createTickets")
    public String createTicketsForDay(HttpSession session, @RequestParam String date1, @RequestParam String timeRange, @RequestParam String serviceType, Model model) throws TicketException {
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        PersonalJobDto personalJobDto = new PersonalJobDto();
        ServiceDto serviceDto = serviceService.findByType(serviceType);
        TicketDto ticketDto = TicketDto.builder()
                .service(serviceDto)
                .date(Date.valueOf(date1).toLocalDate())
                .state(TicketStates.AVAILABLE.toString()).build();
        if (isAppointment(serviceType)) {
             personalJobDto = (PersonalJobDto) session.getAttribute("personalJob");
            if (personalJobDto != null) {
                ticketDto.setPersonalJob(personalJobDto);
                scheduleDtoList = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), date1); //find schedule for get time
            }
        } else {
            scheduleDtoList = scheduleService.findByDateAndServiceId(serviceDto.getId(), Date.valueOf(date1));
        }

        ScheduleDto scheduleDto = scheduleDtoList.getFirst();
        ticketDto.setTime(scheduleDto.getStartTime());
        ticketService.createTicketsForDay(scheduleDto, ticketDto, Integer.valueOf(timeRange)); //create tickets
        List<TicketDto> ticketDtoList = new ArrayList<>();
        if (isAppointment(serviceType)&&personalJobDto!=null) {
            ticketDtoList = ticketService.findByPersonalJobIdAndDate(personalJobDto.getId(), Date.valueOf(date1)); //find created tickets
        } else {
            ticketDtoList = ticketService.findByServiceIdAndDate(serviceDto.getId(), Date.valueOf(scheduleDto.getDate()));
        }

        model.addAttribute("tickets", ticketDtoList);
        return "adminFoundTicketsPage";
    }


    @PostMapping(value = "/createTicketsForPeriod")
    public String createTicketsForPeriod(HttpSession session, @RequestParam String date1, @RequestParam String date2, @RequestParam String timeRange, @RequestParam String serviceType, Model model) throws TicketException {

        List<ScheduleDto> scheduleDtoList=new ArrayList<>();
        ServiceDto serviceDto = serviceService.findByType(serviceType);
        List<TicketDto> ticketDtoList = new ArrayList<>();
        if (isAppointment(serviceType)){
            PersonalJobDto personalJobDto = (PersonalJobDto) session.getAttribute("personalJob");
            scheduleDtoList = scheduleService.findByDatePeriodAndPersonalIdNoDayOff(Date.valueOf(date1), Date.valueOf(date2), personalJobDto.getId());
            ticketService.createTicketsForPeriod(scheduleDtoList, serviceDto, personalJobDto, Integer.valueOf(timeRange));
            ticketDtoList = ticketService.findByPersonalJobAndDatePeriod(personalJobDto.getId(), Date.valueOf(date1), Date.valueOf(date2));
        } else {
            scheduleDtoList = scheduleService.findByDatePeriodAndServiceIdNoDayOff(Date.valueOf(date1), Date.valueOf(date2),serviceDto.getId());
        }

//        Set<String> dates = ticketService.findDates(personalJobDto.getId());



        model.addAttribute("tickets", ticketDtoList);

        return "adminFoundTicketsPage";
    }





    private boolean isAppointment(String serviceType) {
        String regex = "doctor \\w+ appointment";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(serviceType);
        return matcher.find();

    }


}
