package com.medCenter.medCenter.service;


import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.model.entity.Service;

import java.util.List;
import java.util.Set;

public interface ServiceService {

    ServiceDto serviceToDto(Service service);

    Service dtoToService(ServiceDto serviceDto);

    List<ServiceDto> findAll();

    List<ServiceDto> findService(String type, Double price);

    Set<String> findAllTypes();

    ServiceDto findByIdDto(Integer id);

    Service findById(Integer id);

    ServiceDto findByType(String type);

    void createService(ServiceDto serviceDto);

    List<ServiceDto> findByTypeLike(String type);




}
