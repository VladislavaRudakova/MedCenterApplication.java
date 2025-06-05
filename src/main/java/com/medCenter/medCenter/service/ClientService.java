package com.medCenter.medCenter.service;

import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.exception.ClientNotFoundException;
import com.medCenter.medCenter.model.entity.Client;

import java.util.List;

public interface ClientService {

    public ClientDto clientToDto(Client client);

    public Client dtoToClient(ClientDto clientDto);

    void createClient(ClientDto clientDto);

    List<ClientDto> findAll();

    List<ClientDto>findByDoctor(Integer personalJobId);

    ClientDto findByUserId(Integer userId) throws ClientNotFoundException;

    ClientDto findByIdDto(Integer id);

    Client findById(Integer id);

    List<ClientDto> findByNameAndSurname(String name, String surname);

    void updateState(String state, Integer clientId);

    void updateClient(ClientDto clientToEdit);

    List<ClientDto> findClients(ClientDto clientDto);

    List<ClientDto> findClientsByDoctor (ClientDto clientDto, Integer personalJobId);

}
