package com.medCenter.medCenter.model.repository;


import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.Service;

import java.util.List;

public interface ClientRepositoryCustom {

    List<Client> findClients (ClientDto clientDto);

    List<Client> findClientsByDoctor (ClientDto clientDto, Integer personalJobId);

}
