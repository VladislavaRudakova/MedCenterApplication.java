package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.MedCenterApplication;
import com.medCenter.medCenter.model.entity.ScheduleStates;
import com.medCenter.medCenter.model.entity.TicketStates;
import com.medCenter.medCenter.service.ScheduleService;
import com.medCenter.medCenter.service.ServiceService;
import com.medCenter.medCenter.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(StartController.class);
    private final ServiceService serviceService;
    private final ScheduleService scheduleService;
    private final TicketService ticketService;

    private List<String> generateTime() { //get times for service search
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
        logger.info("START METHOD BEGIN");
        List<String> times = generateTime();
        LocalDate date = LocalDate.now();
        scheduleService.updateStateByDate(ScheduleStates.CLOSED.toString(), Date.valueOf(date));//close all schedules where date is before actual date
        ticketService.updateStateByDate(TicketStates.CLOSED.toString(), Date.valueOf(date));//close all tickets where date is before actual date
        Set<String> service = serviceService.findAllTypes();
        model.addAttribute("times", times);
        model.addAttribute("serviceTypes", service);
        return "startPage";
    }


}
