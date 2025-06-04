package com.medCenter.medCenter.service.impl;

import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.dto.UserDto;
import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.User;
import com.medCenter.medCenter.model.repository.ClientRepository;
import com.medCenter.medCenter.service.ClientService;
import com.medCenter.medCenter.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserService userService;


    public ClientDto clientToDto(Client client) {
        ClientDto clientDto = ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .telephoneNumber(client.getTelephoneNumber())
                .build();
        if (client.getState() != null) {
            clientDto.setState(client.getState());
        }
        if (client.getUser() != null) {
            UserDto userDto = userService.userToDto(client.getUser());
            clientDto.setUser(userDto);
        }
        return clientDto;
    }

    public Client dtoToClient(ClientDto clientDto) {
        Client client = Client.builder()
                .name(clientDto.getName())
                .surname(clientDto.getSurname())
                .telephoneNumber(clientDto.getTelephoneNumber())
                .build();
        if (clientDto.getUser() != null) {
            User user = userService.findById(clientDto.getUser().getId());
            client.setUser(user);
        }
        if (clientDto.getState() != null) {
            client.setState(clientDto.getState());
        }
        return client;
    }

    @Transactional
    @Override
    public void createClient(ClientDto clientDto) {
        Client client = dtoToClient(clientDto);
        clientRepository.save(client);
    }

    @Override
    public List<ClientDto> findAll() {
        List<Client> clients = clientRepository.findAll();
        List<ClientDto> clientDtoList = new ArrayList<>();

        for (Client client : clients) {
            ClientDto clientDto = clientToDto(client);
            clientDtoList.add(clientDto);
        }
        return clientDtoList;
    }

    @Override
    public ClientDto findByUserId(Integer userId) {
        Client client = clientRepository.findByUserId(userId);
        if (client == null) {
            throw new EntityNotFoundException();
        }
        return clientToDto(client);
    }

    @Override
    public Client findById(Integer id) {
        return clientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ClientDto findByIdDto(Integer id) {
        Client client = clientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return clientToDto(client);
    }

    @Override
    public List<ClientDto> findByNameAndSurname(String name, String surname) {
        List<Client> clients = clientRepository.findByNameAndSurname(name, surname);
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clients) {
            ClientDto clientDto = clientToDto(client);
            clientDtoList.add(clientDto);
        }
        return clientDtoList;
    }

    @Transactional
    @Override
    public void updateState(String state, Integer clientId) {
        clientRepository.updateState(state, clientId);
    }

    @Transactional
    @Override
    public void updateClient(ClientDto clientToEdit) {
        clientRepository.updateClient(clientToEdit.getName(), clientToEdit.getSurname(), clientToEdit.getTelephoneNumber(),
                clientToEdit.getState(), clientToEdit.getId());
    }

    @Override
    public List<ClientDto> findClients(ClientDto clientDto) {
       List<Client>clients = clientRepository.findClients(clientDto);
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clients) {
            ClientDto clientDto1 = clientToDto(client);
            clientDtoList.add(clientDto1);
        }
        return clientDtoList;
    }

    @Override
    public List<ClientDto> findClientsByDoctor(ClientDto clientDto, Integer personalJobId) {
        List<Client>clients = clientRepository.findClientsByDoctor(clientDto,personalJobId);
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clients) {
            ClientDto clientDto1 = clientToDto(client);
            clientDtoList.add(clientDto1);
        }
        return clientDtoList;
    }

}
