package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/")
@PreAuthorize("isAuthenticated()")
public class AdminServiceController {

    private final ServiceService serviceService;
    private final PersonalJobService personalJobService;

    @GetMapping(value = "/serviceOperations") //get service search form
    public String findAllPersonal(Model model) {
        model.addAttribute("service", new ServiceDto());
        return "adminFindServicePage";
    }

    @PostMapping(value = "/findAllService")
    public String findAllService(Model model) {
        List<ServiceDto> services = serviceService.findAll();
        model.addAttribute("services", services);
        return "adminFoundServicePage";
    }

    @PostMapping(value = "/findService")
    public String findService(Model model, @ModelAttribute ServiceDto service) {
        List<ServiceDto> services = serviceService.findService(service.getType(), service.getPrice()); //dynamic search
        model.addAttribute("services", services);
        return "adminFoundServicePage";
    }

    @GetMapping(value = "/createService")
    public String createServiceForm() { //getting service creating form
        return "adminCreateServicePage";
    }

    @PostMapping(value = "/createService")
    public String createService(@RequestParam String type, @RequestParam Double price, Model model) {
        ServiceDto serviceDto = ServiceDto.builder()
                .type(type)
                .price(price)
                .build();
        serviceService.createService(serviceDto);
        ServiceDto serviceDto1 = serviceService.findByType(type);
        model.addAttribute("service", serviceDto1);
        return "adminCreatedServicePage";
    }


    @PostMapping(value = "/findDoctors")
    public String findDoctorsByService(Model model, @RequestParam Integer serviceId) {
        ServiceDto serviceDto = serviceService.findByIdDto(serviceId);
        String serviceType = serviceDto.getType();
        String result = serviceType.replaceFirst(" appointment$", "");
        List<PersonalJobDto> personalJobDtoList = personalJobService.findByJob(result);
        model.addAttribute("personalList", personalJobDtoList);
        model.addAttribute("personalJob", new PersonalJobDto());
        return "adminFoundPersonalPage";
    }


}
