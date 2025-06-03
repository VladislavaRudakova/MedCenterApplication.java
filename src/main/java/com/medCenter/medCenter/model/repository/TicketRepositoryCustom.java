package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.Ticket;

import java.util.List;

public interface TicketRepositoryCustom {

    List<Ticket> findTickets (TicketDto ticketDto);




}
