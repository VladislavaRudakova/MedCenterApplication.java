package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ScheduleDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.exception.ScheduleNotFoundException;
import com.medCenter.medCenter.model.entity.Roles;
import com.medCenter.medCenter.model.entity.ScheduleStates;
import com.medCenter.medCenter.model.entity.TicketSubStates;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.ClientService;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ScheduleService;
import com.medCenter.medCenter.service.TicketService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("doctor/")
@RequiredArgsConstructor
public class DoctorStartController {

    private static final Logger logger = LogManager.getLogger(DoctorStartController.class);

    private final TicketService ticketService;
    private final ScheduleService scheduleService;
    private final PersonalJobService personalJobService;
    private final ClientService clientService;

    @GetMapping("/doctorStart")
    public String findTickets(HttpSession session, Model model, @AuthenticationPrincipal UserDetailsImpl user) {
        logger.info("DOCTOR FIND TICKETS BEGIN");
        PersonalJobDto personalJob = personalJobService.findByUserId(user.getUser().getId());
        List<TicketDto> tickets = ticketService.findActualByPersonalJob(personalJob.getId()); //find tickets to show its on page
        model.addAttribute("tickets", tickets);
        List<ScheduleDto> scheduleList = new ArrayList<>();
        try {
            scheduleList = scheduleService.findByUserId(user.getUser().getId(), ScheduleStates.OPEN.toString()); //find schedule to show it on page
        } catch (ScheduleNotFoundException e) {
            return "doctorStartPage";
        }
        model.addAttribute("personalJobId", personalJob.getId());
        model.addAttribute("scheduleList", scheduleList);
        return "doctorStartPage";
    }

    @PostMapping("/findPatients")
    public String findPatients(Model model, @AuthenticationPrincipal UserDetailsImpl user,
                               @ModelAttribute ClientDto clientDto, @RequestParam Integer personalJobId) {
        logger.info("DOCTOR FIND PATIENTS BEGIN");
        logger.info("CLIENT DTO RECEIVED: " + clientDto);
        List<ClientDto> clientDtoList = clientService.findClientsByDoctor(clientDto, personalJobId);
        logger.info("CLIENTS FOUND: " + clientDto);
        model.addAttribute("clients", clientDtoList);
        return "adminFoundClientsPage";
    }

    @GetMapping("/getFindPatientsForm")
    public String getFindPatientsForm(Model model, @AuthenticationPrincipal UserDetailsImpl user) {
        logger.info("DOCTOR GET FORM BEGIN");
        PersonalJobDto personalJob = personalJobService.findByUserId(user.getUser().getId());
        model.addAttribute("role", user.getUser().getRole());
        model.addAttribute("clientToFind", new ClientDto());
        model.addAttribute("personalJobId", personalJob.getId());
        return "adminFindClientsPage";
    }

    @PostMapping(value = "/requestForCancellation")
    public String cancelTicket(@RequestParam Integer personalJobId, @RequestParam Integer ticketId, Model model) {
        ticketService.updateSubStateAndRole(TicketSubStates.REQUEST_FOR_CANCELLATION.toString(), Roles.ROLE_DOCTOR.toString(), ticketId); //send request for ticket cancellation
        List<TicketDto> tickets = ticketService.findActualByPersonalJob(personalJobId); //reload updated tickets on page
        model.addAttribute("tickets", tickets);
        return "clientTicketsPage";
    }


}
