package com.medCenter.medCenter.service;

import com.medCenter.medCenter.dto.PersonalDto;
import com.medCenter.medCenter.model.entity.Personal;

import java.util.List;

public interface PersonalService {

    PersonalDto personalToDto(Personal personal);

    Personal dtoToPersonal(PersonalDto personalDto);

    void createPersonal(PersonalDto personalDto);

    List<PersonalDto> findByNameSurnameBirthDate(String name, String surname, String birthDate);


}
