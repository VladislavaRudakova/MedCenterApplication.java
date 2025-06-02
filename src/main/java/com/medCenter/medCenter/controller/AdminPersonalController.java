package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.DepartmentDto;
import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.PersonalJobWithoutPersonalDto;
import com.medCenter.medCenter.dto.UserDto;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.PersonalJobStates;
import com.medCenter.medCenter.model.entity.Roles;
import com.medCenter.medCenter.service.DepartmentService;
import com.medCenter.medCenter.service.PersonalJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("admin/")
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
        PersonalJobStates [] personalJobStates = PersonalJobStates.values();
        List<String> personalJobStatesList = new ArrayList<>();
        for (PersonalJobStates personalJobState : personalJobStates){
            personalJobStatesList.add(personalJobState.toString());
        }
        System.out.println("PERSONAL STATES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+personalJobStatesList);
        model.addAttribute("personalStates", personalJobStatesList);
        return "adminFoundPersonalPage";
    }

    @PostMapping(value = "/findPersonal")
    public String findPersonal(Model model, @ModelAttribute PersonalJobWithoutPersonalDto personalJobDto) {
        List<PersonalJobDto> personalJobs = personalJobService.findPersonalJob(personalJobDto);
        model.addAttribute("personalList", personalJobs);
        PersonalJobStates [] personalJobStates = PersonalJobStates.values();
        List<String> personalJobStatesList = new ArrayList<>();
        for (PersonalJobStates personalJobState : personalJobStates){
          personalJobStatesList.add(personalJobState.toString());
        }

        System.out.println("PERSONAL STATES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+personalJobStatesList);
        model.addAttribute("personalStates", personalJobStatesList);

        return "adminFoundPersonalPage";
    }

    @GetMapping(value = "/createPersonal")
    public String createPersonalForm(Model model) {
        List<DepartmentDto> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("personalJob", new PersonalJobDto());
        return "adminCreatePersonalPage";
    }

    @PostMapping(value = "/createPersonal")
    public String createPersonal(@ModelAttribute PersonalJobDto personalJob, Model model) {
        System.out.println("PERSONAL to SAVE "+personalJob);
        personalJobService.createPersonalJob(personalJob);
        List<PersonalJobDto>personalJobDtoList=personalJobService.findByNameSurnameJob(personalJob.getPersonal().getName(),personalJob.getPersonal().getSurname(),personalJob.getJobTitle());
        model.addAttribute("personalList",personalJobDtoList.getLast());
        return "adminFoundPersonalPage";
    }

    @PostMapping(value = "/personalRegistration")
    public String personalReg(Model model, @RequestParam Integer personalJobId) {
        System.out.println("PERSONALJOB ID!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+personalJobId);
        Roles[] roles = Roles.values();
        List<String> rolesList = new ArrayList<>();
        for (Roles role : roles){
            rolesList.add(role.toString());
        }
        model.addAttribute("roles", rolesList);
        model.addAttribute("user", new UserDto());
        model.addAttribute("personalJobId", personalJobId);
        return "registrationPage";
    }


}
