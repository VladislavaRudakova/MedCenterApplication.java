package com.medCenter.medCenter.service.impl;

import com.medCenter.medCenter.dto.*;
import com.medCenter.medCenter.model.entity.Department;
import com.medCenter.medCenter.model.entity.Personal;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.Ticket;
import com.medCenter.medCenter.model.repository.DepartmentRepository;
import com.medCenter.medCenter.model.repository.PersonalJobRepository;
import com.medCenter.medCenter.model.repository.PersonalRepository;
import com.medCenter.medCenter.service.DepartmentService;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.PersonalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class PersonalJobServiceImpl implements PersonalJobService {

    private final PersonalJobRepository personalJobRepository;
    private final PersonalService personalService;
    private final PersonalRepository personalRepository;
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;


    @Override
    public PersonalJobDto personalJobToDto(PersonalJob personalJob) {
        PersonalDto personalDto = personalService.personalToDto(personalJob.getPersonal());
        DepartmentDto departmentDto = departmentService.departmentToDto(personalJob.getDepartment());
        PersonalJobDto personalJobDto = PersonalJobDto.builder()
                .id(personalJob.getId())
                .personal(personalDto)
                .department(departmentDto)
                .jobTitle(personalJob.getJobTitle())
                .build();

        if (personalJob.getTickets() != null) {
            List<TicketDto> ticketDtoList = new ArrayList<>();
            for (Ticket ticket : personalJob.getTickets()) {
                TicketDto ticketDto = TicketDto.builder()
                        .id(ticket.getId())
                        .date(ticket.getDate().toLocalDate())
                        .time(ticket.getTime().toLocalTime())
                        .state(ticket.getState())
                        .build();

                ticketDtoList.add(ticketDto);
            }
            personalJobDto.setTickets(ticketDtoList);
        }
        return personalJobDto;
    }


    public PersonalJob dtoToPersonalJobWithCreatePersonal(PersonalJobDto personalJobDto) {

        personalService.createPersonal(personalJobDto.getPersonal());
        List<Personal> personalList = personalRepository.findByNameSurnameAndBirthDate(personalJobDto.getPersonal().getName(),
                personalJobDto.getPersonal().getSurname(),
                Date.valueOf(personalJobDto.getPersonal().getBirthDate()));

        Department department = departmentRepository.findByName(personalJobDto.getDepartment().getName());
        return PersonalJob.builder()
                .personal(personalList.getLast())
                .jobTitle(personalJobDto.getJobTitle())
                .department(department)
                .build();
    }


    @Override
    public List<PersonalJobDto> findAll() {
        List<PersonalJob> personalJobList = personalJobRepository.findAll();
        List<PersonalJobDto> personalJobDtoList = new ArrayList<>();

        for (PersonalJob personalJob : personalJobList) {
            PersonalJobDto personalJobDto = personalJobToDto(personalJob);
            personalJobDtoList.add(personalJobDto);
        }
        return personalJobDtoList;
    }

    @Override
    public List<PersonalJobDto> findPersonalJob(PersonalJobWithoutPersonalDto personalJob) {
        List<PersonalJob> personalJobList = personalJobRepository.findPersonalJob(personalJob);
        List<PersonalJobDto> personalJobDtoList = new ArrayList<>();

        for (PersonalJob personalJob1 : personalJobList) {
            PersonalJobDto personalJobDto1 = personalJobToDto(personalJob1);
            personalJobDtoList.add(personalJobDto1);
        }
        return personalJobDtoList;
    }

    @Override
    public PersonalJob findById(Integer id) {
        return personalJobRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public PersonalJobDto findByIdDto(Integer id) {
        PersonalJob personalJob = personalJobRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return personalJobToDto(personalJob);
    }

    @Override
    public PersonalJobDto findByUserId(Integer userId) {
        PersonalJob personalJob = personalJobRepository.findByUserId(userId);
        return personalJobToDto(personalJob);
    }


    @Override
    public List<PersonalJobDto> findByJob(String job) {
        List<PersonalJob> personalJobList = personalJobRepository.findByJob(job);
        List<PersonalJobDto> personalJobDtoList = new ArrayList<>();

        for (PersonalJob personalJob : personalJobList) {
            PersonalJobDto personalJobDto = personalJobToDto(personalJob);
            personalJobDtoList.add(personalJobDto);
        }
        return personalJobDtoList;
    }

    @Override
    public Set<String> findAllJob() {
        return personalJobRepository.findAllJobs();
    }


    @Override
    public Set<String> findJobsNearly(String job) {
        return personalJobRepository.findJobsNearly(job);
    }

    @Override
    public List<PersonalJobDto> findByNameSurnameJob(String name, String surname, String job) {
        List<PersonalJob> personalJobList = personalJobRepository.findByNameSurnameJob(name, surname, job);
        List<PersonalJobDto> personalJobDtoList = new ArrayList<>();

        for (PersonalJob personalJob : personalJobList) {
            PersonalJobDto personalJobDto = personalJobToDto(personalJob);
            personalJobDtoList.add(personalJobDto);
        }
        return personalJobDtoList;
    }

    @Transactional
    @Override
    public void createPersonalJob(PersonalJobDto personalJobDto) {

        PersonalJob personalJob = dtoToPersonalJobWithCreatePersonal(personalJobDto);

        System.out.println("PERSONAL JOB TO SAVE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + personalJob);
        personalJobRepository.save(personalJob);

    }

    @Transactional
    @Override
    public void updateUserId(Integer userId, Integer personalJobId) {
        personalJobRepository.updateUserId(userId, personalJobId);
    }

    @Transactional
    @Override
    public void updatePersonalJob(PersonalJobDto personalJobDto) {
        personalJobRepository.updatePersonalJob(personalJobDto.getPersonal().getName(),
                personalJobDto.getPersonal().getSurname(), Date.valueOf(personalJobDto.getPersonal().getBirthDate()),
                personalJobDto.getPersonal().getExperience(), Date.valueOf(personalJobDto.getPersonal().getEmploymentDate()),
                personalJobDto.getJobTitle(), personalJobDto.getDepartment().getId(), personalJobDto.getId());
    }

    @Override
    public void updateDismissalDate(Date dismissalDate, Integer personalJobId) {
        personalJobRepository.updateDismissalDate(dismissalDate, personalJobId);
    }


}
