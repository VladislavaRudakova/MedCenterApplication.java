package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.*;
import com.medCenter.medCenter.model.entity.PersonalJobStates;
import com.medCenter.medCenter.model.entity.Roles;
import com.medCenter.medCenter.service.DepartmentService;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.PersonalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("admin/")
@RequiredArgsConstructor
public class AdminPersonalController {

    private final PersonalJobService personalJobService;
    private final DepartmentService departmentService;
    private final PersonalService personalService;

    @GetMapping(value = "/personalOperations")
    public String findAllPersonal(Model model) {
        Set<String> jobs = personalJobService.findAllJob();
        model.addAttribute("jobs", jobs);
        PersonalJobStates[] personalJobStates = PersonalJobStates.values();
        List<String> personalJobStatesList = new ArrayList<>();
        for (PersonalJobStates personalJobState : personalJobStates) {
            personalJobStatesList.add(personalJobState.toString());
        }
        model.addAttribute("personalStates", personalJobStatesList);
        model.addAttribute("personalJob", new PersonalJobWithoutPersonalDto());
        return "adminFindPersonalPage";
    }

    @PostMapping(value = "/findAllPersonal")
    public String foundAllPersonal(Model model) {
        List<DepartmentDto> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("personalJob", new PersonalJobDto());
        List<PersonalJobDto> personalJobs = personalJobService.findAll();
        model.addAttribute("personalList", personalJobs);
        PersonalJobStates[] personalJobStates = PersonalJobStates.values();
        List<String> personalJobStatesList = new ArrayList<>();
        for (PersonalJobStates personalJobState : personalJobStates) {
            personalJobStatesList.add(personalJobState.toString());
        }
        model.addAttribute("personalStates", personalJobStatesList);
        return "adminFoundPersonalPage";
    }

    @PostMapping(value = "/findPersonal")
    public String findPersonal(Model model, @ModelAttribute PersonalJobWithoutPersonalDto personalJobDto) {
        List<DepartmentDto> departments = departmentService.findAll();
        model.addAttribute("departments", departments); //department list for search select
        model.addAttribute("personalJob", new PersonalJobDto()); //object for receiving data in Edit section
        List<PersonalJobDto> personalJobs = personalJobService.findPersonalJob(personalJobDto);
        model.addAttribute("personalList", personalJobs); // list of found personal
        PersonalJobStates[] personalJobStates = PersonalJobStates.values();
        List<String> personalJobStatesList = new ArrayList<>();
        for (PersonalJobStates personalJobState : personalJobStates) {
            personalJobStatesList.add(personalJobState.toString());
        }
        model.addAttribute("personalStates", personalJobStatesList); //states list for select in Edit section
        return "adminFoundPersonalPage";
    }

    @GetMapping(value = "/createPersonal") //getting form for creating personal
    public String createPersonalForm(Model model) {
        List<DepartmentDto> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        model.addAttribute("personalJob", new PersonalJobDto());
        return "adminCreatePersonalPage";
    }

    @PostMapping(value = "/createPersonal")
    public String createPersonal(@ModelAttribute PersonalJobDto personalJob, Model model) {
        personalJob.setState(PersonalJobStates.ACTIVE.toString());
        personalJobService.createPersonalJob(personalJob); //create personal
        List<PersonalJobDto> personalJobDtoList = personalJobService.findByNameSurnameJob(personalJob.getPersonal().getName(), //find created
                personalJob.getPersonal().getSurname(), personalJob.getJobTitle());
        model.addAttribute("personalList", personalJobDtoList.getLast());
        model.addAttribute("personalJob", new PersonalJobDto());
        return "adminFoundPersonalPage";
    }

    @PostMapping(value = "/personalRegistration") //personal registration by admin
    public String personalReg(Model model, @RequestParam Integer personalJobId) {
        Roles[] roles = Roles.values();
        List<String> rolesList = new ArrayList<>();
        for (Roles role : roles) { //roles list for select user role
            rolesList.add(role.toString());
        }
        model.addAttribute("roles", rolesList);
        model.addAttribute("user", new UserDto());
        model.addAttribute("personalJobId", personalJobId);
        return "registrationPage";
    }

    @PostMapping(value = "/editPersonal")
    public String editPersonal(@ModelAttribute PersonalJobDto personalJob, @RequestParam Integer personalJobId, Model model) {
        PersonalJobDto personalJobDto = personalJobService.findByIdDto(personalJobId);
        PersonalDto personal = personalJobDto.getPersonal();
        PersonalDto personal1 = personalJob.getPersonal();
        if (personal1.getBirthDate() == null) { //if null set existing date
            personal1.setBirthDate(personal.getBirthDate());
        }
        if (personal1.getEmploymentDate() == null) { //if null set existing date
            personal1.setEmploymentDate(personal.getEmploymentDate());
        }
        if (personal.getDismissalDate() != null) { //if not null use individual method for updating
            personalService.updateDismissalDate(Date.valueOf(personalJob.getPersonal().getDismissalDate()), personalJob.getId());
        }
        personal1.setId(personal.getId());
        personalService.updatePersonal(personal1); //update personal at first
        personalJob.setId(personalJobDto.getId());
        personalJobService.updatePersonalJob(personalJob);//update personalJob
        personalJobDto = personalJobService.findByIdDto(personalJobDto.getId());
        model.addAttribute("personalJob", personalJobDto);
        return "adminFoundPersonalPage";
    }


}
