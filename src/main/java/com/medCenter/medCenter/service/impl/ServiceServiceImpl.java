package com.medCenter.medCenter.service.impl;

import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.model.entity.Ticket;
import com.medCenter.medCenter.model.repository.ServiceRepository;
import com.medCenter.medCenter.service.ServiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceDto serviceToDto(com.medCenter.medCenter.model.entity.Service service) {
        ServiceDto serviceDto = ServiceDto.builder()
                .id(service.getId())
                .type(service.getType())
                .price(service.getPrice()).build();
        if (service.getState() != null) {
            serviceDto.setState(service.getState());
        }
        if (service.getTickets() != null) {
            List<TicketDto> ticketDtoList = new ArrayList<>();
            for (Ticket ticket : service.getTickets()) {
                TicketDto ticketDto = TicketDto.builder()
                        .id(ticket.getId())
                        .date(ticket.getDate().toLocalDate())
                        .time(ticket.getTime().toLocalTime())
                        .state(ticket.getState())
                        .build();

                ticketDtoList.add(ticketDto);
            }
            serviceDto.setTickets(ticketDtoList);
        }

        return serviceDto;

    }

    @Override
    public com.medCenter.medCenter.model.entity.Service dtoToService(ServiceDto serviceDto) { //!!!!!

        return null;
    }


    @Override
    public List<ServiceDto> findAll() {
        List<com.medCenter.medCenter.model.entity.Service> services = serviceRepository.findAll();
        List<ServiceDto> serviceDtoList = new ArrayList<>();
        for (com.medCenter.medCenter.model.entity.Service service : services) {
            ServiceDto serviceDto = serviceToDto(service);
            serviceDtoList.add(serviceDto);
        }
        return serviceDtoList;
    }

    @Override
    public List<ServiceDto> findService(String type, Double price) {
        List<com.medCenter.medCenter.model.entity.Service> services = serviceRepository.findService(type, price);
        List<ServiceDto> serviceDtoList = new ArrayList<>();
        for (com.medCenter.medCenter.model.entity.Service service : services) {
            ServiceDto serviceDto = serviceToDto(service);
            serviceDtoList.add(serviceDto);
        }
        return serviceDtoList;
    }

    @Override
    public Set<String> findAllTypes() {
        return serviceRepository.findAllTypes();
    }

    @Override
    public ServiceDto findByIdDto(Integer id) {
        com.medCenter.medCenter.model.entity.Service service = serviceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return serviceToDto(service);
    }

    @Override
    public com.medCenter.medCenter.model.entity.Service findById(Integer id) {
        return serviceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ServiceDto findByType(String type) {
        com.medCenter.medCenter.model.entity.Service service = serviceRepository.findByType(type);

        return serviceToDto(service);
    }

    @Transactional
    @Override
    public void createService(ServiceDto serviceDto) {

        com.medCenter.medCenter.model.entity.Service service = com.medCenter.medCenter.model.entity.Service.builder()
                .type(serviceDto.getType())
                .price(serviceDto.getPrice()).build();
        serviceRepository.save(service);
    }

    @Transactional
    @Override
    public void updateService(ServiceDto serviceDto) {
        serviceRepository.updateService(serviceDto.getType(), serviceDto.getPrice(), serviceDto.getState(), serviceDto.getId());
    }

    @Override
    public List<ServiceDto> findByTypeLike(String type) {
        List<com.medCenter.medCenter.model.entity.Service> services = serviceRepository.findByTypeLike(type);
        List<ServiceDto> serviceDtoList = new ArrayList<>();
        for (com.medCenter.medCenter.model.entity.Service service : services) {
            ServiceDto serviceDto = ServiceDto.builder()
                    .id(service.getId())
                    .type(service.getType())
                    .price(service.getPrice())
                    .state(service.getState()).build();
            serviceDtoList.add(serviceDto);
        }
        return serviceDtoList;
    }


}
