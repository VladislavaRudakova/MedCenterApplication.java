package com.medCenter.medCenter.controller;

import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.model.entity.ClientStates;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin/")
@RequiredArgsConstructor
public class AdminClientController {

    private final ClientService clientService;
    private static final Logger logger = LogManager.getLogger(AdminClientController.class);

    @GetMapping(value = "/clientOperations")
    public String findAllPersonal(Model model, @AuthenticationPrincipal UserDetailsImpl user) { //getting search form
        List<String> clientStates = getClientStates();
        model.addAttribute("clientStates", clientStates);
        model.addAttribute("clientToFind", new ClientDto()); //object for data receiving
        return "adminFindClientsPage";
    }


    @PostMapping(value = "/findAllClients")
    public String findAllClients(Model model) {
        logger.info("FIND ALL CLIENTS BEGIN");
        ClientStates[] clientStates = ClientStates.values();
        List<String> clientStatesList = new ArrayList<>();
        for (ClientStates clientState : clientStates) { //get states list
            clientStatesList.add(clientState.toString());
        }
        model.addAttribute("clientStates", clientStatesList);
        model.addAttribute("clientToEdit", new ClientDto()); //object for data receiving
        List<ClientDto> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "adminFoundClientsPage";
    }

    @PostMapping(value = "/findClient")
    public String findClient(Model model, @ModelAttribute ClientDto client) {
        logger.info("FIND CLIENT BEGIN");
        logger.info("CLIENT DTO RECEIVED "+client);
        List<String> clientStatesList=getClientStates();
        model.addAttribute("clientStates", clientStatesList);
        model.addAttribute("clientToEdit", new ClientDto());
        List<ClientDto> clients = clientService.findClients(client);
        model.addAttribute("clients", clients);
        return "adminFoundClientsPage";
    }

    @PostMapping(value = "/editClient")
    public String editClient(Model model, @ModelAttribute ClientDto clientToEdit, @RequestParam Integer clientId) {
        logger.info("EDIT CLIENT BEGIN");
        clientToEdit.setId(clientId);
        clientService.updateClient(clientToEdit);
        ClientDto clientDto = clientService.findByIdDto(clientId);
        logger.info("UPDATED CLIENT "+clientDto);
        model.addAttribute("clientUpdated", clientDto);
        return "adminFoundClientsPage";
    }

    private List<String> getClientStates(){
        ClientStates[] clientStates = ClientStates.values();
        List<String> clientStatesList = new ArrayList<>();
        for (ClientStates clientState : clientStates) { //get states list
            clientStatesList.add(clientState.toString());
        }
        return clientStatesList;
    }


}
