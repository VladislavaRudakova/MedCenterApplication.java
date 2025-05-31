package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ScheduleDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.exception.ScheduleNotFoundException;
import com.medCenter.medCenter.model.entity.ScheduleStates;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.ClientService;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ScheduleService;
import com.medCenter.medCenter.service.TicketService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("doctor/")
@RequiredArgsConstructor
public class DoctorStartController {

    private final TicketService ticketService;
    private final ScheduleService scheduleService;
    private final PersonalJobService personalJobService;

    @GetMapping("/doctorStart")
    public String findTickets(HttpSession session, Model model, @AuthenticationPrincipal UserDetailsImpl user) {
        PersonalJobDto personalJob = personalJobService.findByUserId(user.getUser().getId());
        List<TicketDto> tickets = ticketService.findActualByPersonalJob(personalJob.getId()); //find tickets to show its on page
        model.addAttribute("tickets", tickets);
        List<ScheduleDto> scheduleList = new ArrayList<>();
        try {
            scheduleList = scheduleService.findByUserId(user.getUser().getId(), ScheduleStates.OPEN.toString()); //find schedule to show it on page
        } catch (ScheduleNotFoundException e) {
            return "doctorStartPage";
        }
        model.addAttribute("scheduleList", scheduleList);
        return "doctorStartPage";
    }

    @GetMapping("/getFindPatientsForm")
    public String findPatients(Model model, @AuthenticationPrincipal UserDetailsImpl user) {
        model.addAttribute(user.getUser().getRole(), "role");
        return "adminFindClientsPage";
    }


}
