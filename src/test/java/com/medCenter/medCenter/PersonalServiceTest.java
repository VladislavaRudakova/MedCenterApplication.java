package com.medCenter.medCenter;

import com.medCenter.medCenter.dto.PersonalDto;
import com.medCenter.medCenter.model.entity.Personal;
import com.medCenter.medCenter.model.repository.PersonalRepository;
import com.medCenter.medCenter.service.PersonalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@SpringBootTest
@Transactional
public class PersonalServiceTest {

    @Autowired
    private PersonalService personalService;
    @Autowired
    private PersonalRepository personalRepository;

    @Test
    public void dtoToPersonal() {
        Assertions.assertNotNull(personalService);

        String name = "testName";
        String surname = "testSurname";
        Date birthDate = Date.valueOf("2000-02-02");
        Date employmentDate = Date.valueOf("2022-02-02");
        Integer experience = 5;

        PersonalDto personalDto = PersonalDto.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toLocalDate())
                .employmentDate(employmentDate.toLocalDate())
                .experience(experience).build();

        Personal personal = personalService.dtoToPersonal(personalDto);
        Assertions.assertNotNull(personal);
        Assertions.assertEquals(personal.getName(), personalDto.getName());
        Assertions.assertEquals(personal.getSurname(), personalDto.getSurname());
        Assertions.assertEquals(personal.getBirthDate(), Date.valueOf(personalDto.getBirthDate()));
        Assertions.assertEquals(personal.getEmploymentDate(), Date.valueOf(personalDto.getEmploymentDate()));
        Assertions.assertEquals(personal.getExperience(), personalDto.getExperience());
    }

    @Test
    public void personalToDto() {
        Assertions.assertNotNull(personalService);
        String name = "testName";
        String surname = "testSurname";
        Date birthDate = Date.valueOf("2000-02-02");
        Date employmentDate = Date.valueOf("2022-02-02");
        Integer experience = 5;

        Personal personal = Personal.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate)
                .employmentDate(employmentDate)
                .experience(experience).build();

        personalRepository.save(personal);

        List<Personal> personalList = personalRepository.findByNameSurnameAndBirthDate(name, surname, birthDate);
        Personal personalSaved = personalList.getLast();

        PersonalDto personalDto = personalService.personalToDto(personalSaved);
        Assertions.assertNotNull(personalDto);
        Assertions.assertEquals(personal.getName(), personalDto.getName());
        Assertions.assertEquals(personal.getSurname(), personalDto.getSurname());
        Assertions.assertEquals(personal.getBirthDate(), Date.valueOf(personalDto.getBirthDate()));
        Assertions.assertEquals(personal.getEmploymentDate(), Date.valueOf(personalDto.getEmploymentDate()));
        Assertions.assertEquals(personal.getExperience(), personalDto.getExperience());

    }

    public void findById() {


    }


    @Test
    public void createPersonal() {

        Assertions.assertNotNull(personalService);

        String name = "testName";
        String surname = "testSurname";
        Date birthDate = Date.valueOf("2000-02-02");
        Date employmentDate = Date.valueOf("2022-02-02");
        Integer experience = 5;

        PersonalDto personalDto = PersonalDto.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toLocalDate())
                .employmentDate(employmentDate.toLocalDate())
                .experience(experience).build();

        personalService.createPersonal(personalDto);

        List<PersonalDto> personalDtoList = personalService.findByNameSurnameBirthDate(name, surname, birthDate.toString());
        PersonalDto personalDtoSaved = personalDtoList.getLast();

        Assertions.assertNotNull(personalDtoSaved);
        Assertions.assertEquals(personalDtoSaved.getName(), name);
        Assertions.assertEquals(personalDtoSaved.getSurname(), surname);
        Assertions.assertEquals(Date.valueOf(personalDtoSaved.getBirthDate()), birthDate);

    }

}
