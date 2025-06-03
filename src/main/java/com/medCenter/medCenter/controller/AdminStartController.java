package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.dto.ScheduleDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.exception.ScheduleNotFoundException;
import com.medCenter.medCenter.model.entity.ScheduleStates;
import com.medCenter.medCenter.model.entity.TicketSubStates;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.ScheduleService;
import com.medCenter.medCenter.service.TicketService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AdminStartController {

    private final TicketService ticketService;
    private final ScheduleService scheduleService;


    @GetMapping(value = "/start")
    public String start(Model model, HttpSession session, @AuthenticationPrincipal UserDetailsImpl user) {

        List<TicketDto> tickets = ticketService.findBySubState(TicketSubStates.REQUEST_FOR_CANCELLATION.toString()); //find tickets for cancellation to show it on page
        session.setAttribute("ticketsForCancellation", tickets);
        session.setAttribute("count", tickets.size());
        List<ScheduleDto> scheduleList = new ArrayList<>();
        try {
            scheduleList = scheduleService.findByUserId(user.getUser().getId(), ScheduleStates.OPEN.toString()); //find actual user schedule to chow it on page
        } catch (ScheduleNotFoundException e) {
            return "adminStartPage";
        }
        model.addAttribute("scheduleList", scheduleList);
        return "adminStartPage";
    }


}
