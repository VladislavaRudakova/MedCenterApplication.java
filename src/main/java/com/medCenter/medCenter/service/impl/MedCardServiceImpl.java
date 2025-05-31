package com.medCenter.medCenter.service.impl;

import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.dto.MedCardDto;
import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.MedCard;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.repository.MedCardRepository;
import com.medCenter.medCenter.service.ClientService;
import com.medCenter.medCenter.service.MedCardService;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ServiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedCardServiceImpl implements MedCardService {

    private final MedCardRepository medCardRepository;
    private final ServiceService serviceService;
    private final PersonalJobService personalJobService;
    private final ClientService clientService;

    @Override
    public MedCardDto medCardToDto(MedCard medCard) {

        ClientDto clientDto = clientService.clientToDto(medCard.getClient());
        MedCardDto medCardDto = MedCardDto.builder()
                .id(medCard.getId())
                .client(clientDto)
                .date(medCard.getDate().toLocalDate())
                .complaints(medCard.getComplaints())
                .diagnosis(medCard.getDiagnosis())
                .appointment(medCard.getAppointment())
                .build();
        if (medCard.getService() != null) {
            ServiceDto serviceDto = serviceService.serviceToDto(medCard.getService());
            medCardDto.setService(serviceDto);
        }
        if (medCard.getPersonalJob() != null) {
            PersonalJobDto personalJobDto = personalJobService.personalJobToDto(medCard.getPersonalJob());
            medCardDto.setPersonalJob(personalJobDto);
        }
        if (medCard.getExaminationResult() != null) {
            medCardDto.setExaminationResult(medCard.getExaminationResult());
        }
        if (medCard.getState() != null) {
            medCardDto.setState(medCard.getState());
        }
        return medCardDto;
    }

    @Override
    public MedCard dtoToMedCard(MedCardDto medCardDto) {

        Client client = clientService.findById(medCardDto.getClient().getId());
        MedCard medCard = MedCard.builder()
                .id(medCardDto.getId())
                .date(Date.valueOf(medCardDto.getDate()))
                .client(client)
                .complaints(medCardDto.getComplaints())
                .appointment(medCardDto.getAppointment())
                .diagnosis(medCardDto.getDiagnosis()).build();
        if (medCardDto.getPersonalJob() != null) {
            PersonalJob personalJob = personalJobService.findById(medCardDto.getPersonalJob().getId());
            medCard.setPersonalJob(personalJob);
        }
        if (medCardDto.getService() != null) {
            com.medCenter.medCenter.model.entity.Service service = serviceService.findById(medCardDto.getService().getId());
            medCard.setService(service);
        }
        if (medCardDto.getExaminationResult() != null) {
            medCard.setExaminationResult(medCardDto.getExaminationResult());
        }
        if (medCardDto.getState() != null) {
            medCard.setState(medCardDto.getState());
        }
        return medCard;
    }

    @Override
    public MedCard findById(Integer id) {
        return medCardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public MedCardDto findByIdDto(Integer id) {
        MedCard medCard = medCardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return medCardToDto(medCard);
    }

    @Transactional
    @Override
    public void createMedCard(MedCardDto medCardDto) {
        MedCard medCard = dtoToMedCard(medCardDto);
        medCardRepository.save(medCard);

    }

    @Override
    public List<MedCardDto> findByClientId(Integer clientId) {
        List<MedCard> medCards = medCardRepository.findByClientId(clientId);
        List<MedCardDto> medCardDtoList = new ArrayList<>();
        for (MedCard medCard : medCards) {
            MedCardDto medCardDto = medCardToDto(medCard);
            medCardDtoList.add(medCardDto);
        }
        return medCardDtoList;
    }

    @Override
    public List<MedCardDto> findByClientIdPersonalJobIdAndDate(Integer clientId, Integer personalJobId, Date date) {
        List<MedCard> medCards = medCardRepository.findByClientIdPersonalJobIdAndDate(clientId, personalJobId, date);
        List<MedCardDto> medCardDtoList = new ArrayList<>();
        for (MedCard medCard : medCards) {
            MedCardDto medCardDto = medCardToDto(medCard);
            medCardDtoList.add(medCardDto);
        }
        return medCardDtoList;
    }


}
