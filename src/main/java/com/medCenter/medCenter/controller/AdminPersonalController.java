package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.DepartmentDto;
import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.PersonalJobWithoutPersonalDto;
import com.medCenter.medCenter.service.DepartmentService;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("admin/")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class AdminPersonalController {

    private final PersonalJobService personalJobService;
    private final DepartmentService departmentService;

    @GetMapping(value = "/personalOperations")
    public String findAllPersonal(Model model) {
        Set<String> jobs = personalJobService.findAllJob();
        model.addAttribute("jobs", jobs);
        model.addAttribute("personalJob", new PersonalJobWithoutPersonalDto());
        return "adminFindPersonalPage";
    }


    @PostMapping(value = "/findAllPersonal")
    public String foundAllPersonal(Model model) {
        List<PersonalJobDto> personalJobs = personalJobService.findAll();
        model.addAttribute("personalList", personalJobs);
        return "adminFoundPersonalPage";
    }

    @PostMapping(value = "/findPersonal")
    public String findPersonal(Model model, @ModelAttribute PersonalJobWithoutPersonalDto personalJobDto) {
        List<PersonalJobDto> personalJobs = personalJobService.findPersonalJob(personalJobDto);
        model.addAttribute("personalList", personalJobs);
        return "adminFoundPersonalPage";
    }

    @GetMapping(value = "/createPersonal")
    public String createPersonalForm(Model model) {
        List<DepartmentDto> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        return "adminCreatePersonalPage";
    }

    @PostMapping(value = "/createPersonal")
    public String createPersonal() {
        return "adminCreatePersonalPage";
    }


}
