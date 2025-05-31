package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("admin/")
@RequiredArgsConstructor
public class AdminClientController {

    private final ClientService clientService;


    @GetMapping(value = "/clientOperations")
    public String findAllPersonal(Model model,  @AuthenticationPrincipal UserDetailsImpl user) {
        model.addAttribute(user.getUser().getRole(), "role");
        return "adminFindClientsPage";
    }



    @PostMapping(value = "/findAllClients")
    public String findAllClients(Model model) {
        List<ClientDto> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "adminFoundClientsPage";
    }

    @PostMapping(value = "/findClient")
    public String findClientByNameSurname(Model model, @RequestParam String name, @RequestParam String surname) {
        List<ClientDto> clients = clientService.findByNameAndSurname(name,surname);
        model.addAttribute("clients", clients);
        return "adminFoundClientsPage";
    }
}
