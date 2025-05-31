package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.dto.TicketDtoLight;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.Service;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ServiceService;
import com.medCenter.medCenter.service.TicketService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class ServiceSearchController {

    private final PersonalJobService personalJobService;
    private final TicketService ticketService;
    private final ServiceService serviceService;

    @PostMapping("/personal")
    public String findByJobDateTime(@RequestParam String serviceType, @RequestParam String date,
                                    @RequestParam String minTime, @RequestParam String maxTime, HttpSession session, Model model) {

        session.setAttribute("ticketDate", Date.valueOf(date).toLocalDate());
        session.setAttribute("minTime", minTime);
        session.setAttribute("maxTime", maxTime);
        session.setAttribute("serviceType", serviceType);

        ServiceDto service = serviceService.findByType(serviceType);
        session.setAttribute("service", service);

        if (isAppointment(serviceType)) {
            List<PersonalJobDto> personalJobs = ticketService.findByServiceDateTime(serviceType, Date.valueOf(date), Time.valueOf(minTime + ":00"), Time.valueOf(maxTime + ":00"), "available");
            session.setAttribute("personalList", personalJobs);
            model.addAttribute("instance","personal");
        } else {
            model.addAttribute("instance","service");
        }

        return "doctorsPage";
    }

    @GetMapping("/personal")
    public String findByJobDateTime1(HttpSession session, Model model) {
       LocalDate date = (LocalDate) session.getAttribute("ticketDate");
        String minTime = (String) session.getAttribute("minTime");
        String maxTime = (String) session.getAttribute("maxTime");
        String serviceType = (String) session.getAttribute("serviceType");

        ServiceDto service = serviceService.findByType(serviceType);
        session.setAttribute("service", service);

        if (isAppointment(serviceType)) {
            List<PersonalJobDto> personalJobs = ticketService.findByServiceDateTime(serviceType, Date.valueOf(date), Time.valueOf(minTime + ":00"), Time.valueOf(maxTime + ":00"), "available");
            session.setAttribute("personalList", personalJobs);
            model.addAttribute("instance","personal");
        }else {
            model.addAttribute("instance","service");
        }
        return "doctorsPage";
    }


    private boolean isAppointment(String serviceType) {
        String regex = "doctor \\w+ appointment";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(serviceType);
        return matcher.find();

    }


}
