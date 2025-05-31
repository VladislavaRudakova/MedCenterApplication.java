package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.PersonalJobWithoutPersonalDto;
import com.medCenter.medCenter.model.entity.PersonalJob;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface PersonalJobRepositoryCustom {

    List<PersonalJob> findPersonalJob(PersonalJobWithoutPersonalDto personal);

}
