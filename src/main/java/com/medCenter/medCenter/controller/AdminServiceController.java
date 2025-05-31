package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.model.entity.Service;
import com.medCenter.medCenter.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/")
@PreAuthorize("isAuthenticated()")
public class AdminServiceController {

private final ServiceService serviceService;


    @GetMapping(value = "/serviceOperations")
    public String findAllPersonal(Model model) {
        model.addAttribute("service",new ServiceDto());
        return "adminFindServicePage";
    }






    @GetMapping(value = "/serviceSearchForm")
    public String getServiceSearchForm(Model model) {

        return "adminFindServicePage";
    }


    @PostMapping(value = "/findAllService")
    public String findAllService(Model model) {
        List<ServiceDto> services = serviceService.findAll();
        System.out.println("SERVICES!!!"+services);
        model.addAttribute("services", services);
        return "adminFoundServicePage";
    }


    @PostMapping(value = "/findService")
    public String findService(Model model, @ModelAttribute ServiceDto service) {
        List<ServiceDto> services = serviceService.findService(service.getType(),service.getPrice());
        model.addAttribute("services",services);
        System.out.println("SERVICES!!!!!!!!!!"+services);
        return "adminFoundServicePage";
    }

    @GetMapping(value = "/createService")
    public String createServiceForm() {

        return "adminCreateServicePage";
    }

    @PostMapping(value = "/createService")
    public String createService(@RequestParam String type, @RequestParam Double price, Model model) {
        System.out.println("TYPE!!!!!!!"+type+"PRICE!!!!"+price);
        ServiceDto serviceDto = ServiceDto.builder()
                .type(type)
                .price(price)
                .build();
        serviceService.createService(serviceDto);
        ServiceDto serviceDto1 = serviceService.findByType(type);
        model.addAttribute("service", serviceDto1);
        return "adminCreatedServicePage";
    }





}
