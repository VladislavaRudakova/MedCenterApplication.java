package com.medCenter.medCenter.service.impl;

import com.medCenter.medCenter.dto.PersonalDto;
import com.medCenter.medCenter.model.entity.Personal;
import com.medCenter.medCenter.model.repository.PersonalJobRepository;
import com.medCenter.medCenter.model.repository.PersonalRepository;
import com.medCenter.medCenter.service.PersonalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalServiceImpl implements PersonalService {

    private final PersonalRepository personalRepository;
    private final PersonalJobRepository personalJobRepository;


    public PersonalDto personalToDto(Personal personal) { //mapping dto to entity
        PersonalDto personalDto = PersonalDto.builder()
                .id(personal.getId())
                .name(personal.getName())
                .surname(personal.getSurname())
                .birthDate(personal.getBirthDate().toLocalDate())
                .employmentDate(personal.getEmploymentDate().toLocalDate())
                .state(personal.getState())
                .experience(personal.getExperience())
                .photo(personal.getPhoto()).build();
        if (personal.getDismissalDate() != null) {
            personalDto.setDismissalDate(personal.getDismissalDate().toLocalDate());
        }
        return personalDto;
    }

    @Override
    public Personal dtoToPersonal(PersonalDto personalDto) { //mapping entity to dto
        return Personal.builder()
                .name(personalDto.getName())
                .surname(personalDto.getSurname())
                .birthDate(Date.valueOf(personalDto.getBirthDate()))
                .employmentDate(Date.valueOf(personalDto.getEmploymentDate()))
                .experience(personalDto.getExperience())
                .build();
    }


    @Override
    public void createPersonal(PersonalDto personalDto) {
        Personal personal = dtoToPersonal(personalDto);
        personalRepository.save(personal);
    }

    @Override
    public PersonalDto findById(Integer id) {
        Personal personal = personalRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return personalToDto(personal);
    }

    @Override
    public List<PersonalDto> findByNameSurnameBirthDate(String name, String surname, String birthDate) {
        List<Personal> personalList = personalRepository.findByNameSurnameAndBirthDate(name, surname, Date.valueOf(birthDate));
        List<PersonalDto> personalDtoList = new ArrayList<>();
        for (Personal personal : personalList) {
            PersonalDto personalDto = personalToDto(personal);
            personalDtoList.add(personalDto);
        }
        return personalDtoList;
    }

    @Transactional
    @Override
    public void updateState(String state, Integer personalId) {
        personalRepository.updateState(state, personalId);
        personalJobRepository.updateState(state, personalId);
    }

    @Transactional
    @Override
    public void updateDismissalDate(Date date, Integer personalId) {
        personalRepository.updateDismissalDate(date, personalId);
    }

    @Transactional
    @Override
    public void updatePersonal(PersonalDto personalDto) {
        personalRepository.updatePersonal(personalDto.getName(), personalDto.getSurname(), Date.valueOf(personalDto.getBirthDate()),
                personalDto.getExperience(), Date.valueOf(personalDto.getEmploymentDate()), personalDto.getId());
    }


}

