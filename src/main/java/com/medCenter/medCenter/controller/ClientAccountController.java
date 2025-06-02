package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.dto.UserDto;
import com.medCenter.medCenter.model.entity.TicketStates;
import com.medCenter.medCenter.model.entity.TicketSubStates;
import com.medCenter.medCenter.securityConfig.UserDetailsImpl;
import com.medCenter.medCenter.service.ClientService;
import com.medCenter.medCenter.service.TicketService;
import com.medCenter.medCenter.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("client/")
@RequiredArgsConstructor
public class ClientAccountController {


    private final TicketService ticketService;
    private final ClientService clientService;
    private final UserService userService;


    @PostMapping(value = "/clientTicket")
    public String clientTicketRegistration(@RequestParam Integer ticketId, Model model, @AuthenticationPrincipal UserDetailsImpl user, HttpSession session) {
        TicketDto ticket = ticketService.findById(ticketId);
        ClientDto client = null;
        try {
            client = clientService.findByUserId(user.getUser().getId());
        } catch (EntityNotFoundException e) {
            session.setAttribute("ticket", ticket);
            return "saveClientPage";
        }
        return updateTicketClient(client, ticket, model);
    }


    @PostMapping(value = "/saveClient")
    public String saveClient(@RequestParam String name, @RequestParam String surname, @RequestParam String telephone,
                             HttpSession session, Model model, @AuthenticationPrincipal UserDetailsImpl user) {

        UserDto userDto = userService.userToDto(user.getUser());
        ClientDto client = ClientDto.builder()
                .name(name)
                .surname(surname)
                .telephoneNumber(telephone)
                .user(userDto)
                .build();
        clientService.createClient(client);
        client = clientService.findByUserId(user.getUser().getId());
        TicketDto ticket = (TicketDto) session.getAttribute("ticket");
        return updateTicketClient(client, ticket, model);
    }

    @Transactional
    private String updateTicketClient(ClientDto client, TicketDto ticket, Model model) {
        ticketService.updateClientAndState(client.getId(), TicketStates.NOT_AVAILABLE.toString(), ticket.getId());
        ticket.setClient(client);
        model.addAttribute("ticket", ticket);
        return "clientTicketPage";
    }


    @GetMapping(value = "/clientTickets")
    public String findAllTickets(@AuthenticationPrincipal UserDetailsImpl user, Model model, HttpSession session) {
        TicketDto ticketDto = (TicketDto) session.getAttribute("ticket");
        Integer clientId = null;
        if (ticketDto!=null){
            clientId = ticketDto.getId();
        } else  {
            ClientDto client = clientService.findByUserId(user.getUser().getId());
            clientId = client.getId();
        }
        List<TicketDto> tickets = ticketService.findByClient(clientId);
        model.addAttribute("tickets", tickets);
        model.addAttribute("clientId", clientId);
        return "clientTicketsPage";
    }


    @PostMapping(value = "/requestForCancellation")
    public String cancelTicket(@RequestParam String clientId, @RequestParam String ticketId, Model model) {

        ticketService.updateSubState(TicketSubStates.REQUEST_FOR_CANCELLATION.toString(), Integer.valueOf(ticketId));
        List<TicketDto> tickets = ticketService.findByClient(Integer.valueOf(clientId));
        model.addAttribute("tickets", tickets);
        return "clientTicketsPage";
    }


}
