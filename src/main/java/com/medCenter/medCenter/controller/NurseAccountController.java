package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("nurse/")
@RequiredArgsConstructor
public class NurseAccountController {

    private final TicketService ticketService;

    @GetMapping("/nurseStart")
    public String findAllNurseTickets(Model model) {
        List<TicketDto> ticketDtoList = ticketService.findWherePersonalIsNull(); //find all tickets witch are not doctor appointment
        System.out.println("NURSE TICKETS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+ ticketDtoList);
        model.addAttribute("tickets", ticketDtoList);
        return "nurseStartPage";
    }


}
