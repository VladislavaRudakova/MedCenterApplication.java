package com.medCenter.medCenter.service;

import com.medCenter.medCenter.dto.MedCardDto;
import com.medCenter.medCenter.model.entity.MedCard;

import java.sql.Date;
import java.util.List;

public interface MedCardService {

    MedCardDto medCardToDto(MedCard medCard);

    MedCard dtoToMedCard(MedCardDto medCardDto);

    MedCard findById(Integer id);

    MedCardDto findByIdDto(Integer id);

    void createMedCard(MedCardDto medCardDto);

    List<MedCardDto> findByClientId(Integer clientId);

    List<MedCardDto> findByClientIdPersonalJobIdAndDate(Integer clientId, Integer personalJobId, Date date);

}
