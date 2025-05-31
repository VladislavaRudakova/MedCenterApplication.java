package com.medCenter.medCenter.service;

import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.PersonalJobWithoutPersonalDto;
import com.medCenter.medCenter.model.entity.PersonalJob;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface PersonalJobService {

    PersonalJobDto personalJobToDto(PersonalJob personalJob);

    PersonalJob dtoToPersonalJobWithCreatePersonal(PersonalJobDto personalJobDto);

    List<PersonalJobDto> findAll();

    List<PersonalJobDto> findPersonalJob(PersonalJobWithoutPersonalDto personalJob);

    PersonalJob findById(Integer id);

    PersonalJobDto findByIdDto(Integer id);

    PersonalJobDto findByUserId(Integer userId);

    List<PersonalJobDto> findByJob(String job);

    Set<String> findAllJob();

//    List<PersonalJob> findByJobDateTime(String job, String date, String startTime, String endTime);

    Set<String> findJobsNearly (String job);

    List<PersonalJobDto> findByNameSurnameJob(String name, String surname, String job);
    void createPersonalJob(PersonalJobDto personalJobDto);

    void updateUserId(Integer userId, Integer personalJobId);

    void updatePersonalJob(PersonalJobDto personalJobDto);

}
