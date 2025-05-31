package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ScheduleDto;
import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.exception.ScheduleExistException;
import com.medCenter.medCenter.exception.ScheduleNotFoundException;
import com.medCenter.medCenter.model.entity.ScheduleStates;
import com.medCenter.medCenter.model.entity.TicketSubStates;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/")
@PreAuthorize("isAuthenticated()")
public class AdminStartController {

    private final TicketService ticketService;
    private final ScheduleService scheduleService;


    @GetMapping(value = "/start")
    public String start(Model model, HttpSession session, @AuthenticationPrincipal UserDetailsImpl user) {

        List<TicketDto> tickets = ticketService.findBySubState(TicketSubStates.REQUEST_FOR_CANCELLATION.toString());
        session.setAttribute("ticketsForCancellation", tickets);
       session.setAttribute("count", tickets.size());
        List<ScheduleDto> scheduleList = new ArrayList<>();
       try {
          scheduleList = scheduleService.findByUserId(user.getUser().getId(), ScheduleStates.OPEN.toString());
       } catch (ScheduleNotFoundException e){
           return "adminStartPage";
       }

        System.out.println("SCHEDULE!!!!!" + scheduleList);
        model.addAttribute("scheduleList", scheduleList);

        return "adminStartPage";

    }





}
