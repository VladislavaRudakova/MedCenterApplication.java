package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.dto.UserDto;
import com.medCenter.medCenter.exception.ClientNotFoundException;
import com.medCenter.medCenter.model.entity.ClientStates;
import com.medCenter.medCenter.model.entity.Roles;
import com.medCenter.medCenter.model.entity.TicketStates;
import com.medCenter.medCenter.model.entity.TicketSubStates;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.ClientService;
import com.medCenter.medCenter.service.TicketService;
import com.medCenter.medCenter.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("client/")
@RequiredArgsConstructor
public class ClientAccountController {

    private static final Logger logger = LogManager.getLogger(ClientAccountController.class);

    private final TicketService ticketService;
    private final ClientService clientService;
    private final UserService userService;


    @PostMapping(value = "/clientTicket")
    public String clientTicketRegistration(@RequestParam Integer ticketId, Model model, @AuthenticationPrincipal UserDetailsImpl user, HttpSession session) throws ClientNotFoundException {
        TicketDto ticket = ticketService.findById(ticketId); //find ticket selected by user
        ClientDto client = null;
        try {
            client = clientService.findByUserId(user.getUser().getId()); //if user is already attached to client find him
        } catch (ClientNotFoundException e) {
            session.setAttribute("ticket", ticket); //if user is not attached to client save new client
            return "saveClientPage";
        }
        return updateTicketClient(client, ticket, model); //make ticket clientId not null
    }


    @PostMapping(value = "/saveClient")
    public String saveClient(@RequestParam String name, @RequestParam String surname, @RequestParam String telephone,
                             HttpSession session, Model model, @AuthenticationPrincipal UserDetailsImpl user) throws ClientNotFoundException {
        UserDto userDto = userService.userToDto(user.getUser());
        ClientDto client = ClientDto.builder()
                .name(name)
                .surname(surname)
                .telephoneNumber(telephone)
                .state(ClientStates.ACTIVE.toString())
                .user(userDto)
                .build();
        clientService.createClient(client); //save new client
        client = clientService.findByUserId(user.getUser().getId()); //find client for get it id
        TicketDto ticket = (TicketDto) session.getAttribute("ticket");
        return updateTicketClient(client, ticket, model); //make ticket clientId not null
    }

    @Transactional
    private String updateTicketClient(ClientDto client, TicketDto ticket, Model model) {
        ticketService.updateClientAndState(client.getId(), TicketStates.NOT_AVAILABLE.toString(), ticket.getId()); // set client id to ticket
        ticket.setClient(client);
        model.addAttribute("ticket", ticket);
        return "clientTicketPage";
    }


    @GetMapping(value = "/clientTickets")
    public String findAllTickets(@AuthenticationPrincipal UserDetailsImpl user, Model model, HttpSession session) {
        TicketDto ticketDto = (TicketDto) session.getAttribute("ticket");
        Integer clientId = null;
        List<TicketDto> tickets = new ArrayList<>();
        if (ticketDto != null) {
            clientId = ticketDto.getClient().getId();
        } else {
            try {
                ClientDto client = clientService.findByUserId(user.getUser().getId());
                clientId = client.getId();
            } catch (ClientNotFoundException e){
                model.addAttribute("tickets",tickets);
                return "clientTicketsPage";
            }

        }
        tickets = ticketService.findActualByClient(clientId); //find tickets to show it in client account
        model.addAttribute("tickets", tickets);
        model.addAttribute("clientId", clientId);
        return "clientTicketsPage";
    }


    @PostMapping(value = "/requestForCancellation")
    public String cancelTicket(@RequestParam Integer clientId, @RequestParam Integer ticketId, Model model) {
        ticketService.updateSubStateAndRole(TicketSubStates.REQUEST_FOR_CANCELLATION.toString(), Roles.ROLE_CLIENT.toString(), ticketId); //send request for ticket cancellation
        List<TicketDto> tickets = ticketService.findByClient(clientId); //reload updated tickets on page
        model.addAttribute("clientId", clientId);
        model.addAttribute("tickets", tickets);
        return "clientTicketsPage";
    }


}
