package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.model.entity.ScheduleStates;
import com.medCenter.medCenter.model.entity.TicketStates;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ScheduleService;
import com.medCenter.medCenter.service.ServiceService;
import com.medCenter.medCenter.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class StartController {

    private final PersonalJobService personalJobService;
    private final ServiceService serviceService;
    private final ScheduleService scheduleService;
    private final TicketService ticketService;

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


    @GetMapping(value = "/")
    public String start(Model model) {
        List<String> times = generateTime();
        LocalDate date = LocalDate.now();
        scheduleService.updateStateByDate(ScheduleStates.CLOSED.toString(), Date.valueOf(date));
        ticketService.updateStateByDate(TicketStates.CLOSED.toString(),Date.valueOf(date));

        Set<String> jobs = personalJobService.findJobsNearly("doctor");
        Set<String> service = serviceService.findAllTypes();

        model.addAttribute("times", times);
        model.addAttribute("jobs", jobs);
        model.addAttribute("serviceTypes", service);
        return "startPage";
    }


}
