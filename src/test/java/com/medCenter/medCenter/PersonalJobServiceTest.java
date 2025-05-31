package com.medCenter.medCenter;

import com.medCenter.medCenter.dto.DepartmentDto;
import com.medCenter.medCenter.dto.PersonalDto;
import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.model.entity.Department;
import com.medCenter.medCenter.model.entity.Personal;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.repository.DepartmentRepository;
import com.medCenter.medCenter.model.repository.PersonalJobRepository;
import com.medCenter.medCenter.model.repository.PersonalRepository;
import com.medCenter.medCenter.service.DepartmentService;
import com.medCenter.medCenter.service.PersonalJobService;
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
public class PersonalJobServiceTest {
    @Autowired
    private PersonalJobService personalJobService;
    @Autowired
    private PersonalService personalService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PersonalRepository personalRepository;
    @Autowired
    public PersonalJobRepository personalJobRepository;


    @Test
    public void personalJobToDto() {
        Assertions.assertNotNull(personalJobService);
        Assertions.assertNotNull(personalService);
        Assertions.assertNotNull(departmentService);
        String name = "testName";
        String surname = "testSurname";
        Date birthDate = Date.valueOf("2000-02-02");
        Date employmentDate = Date.valueOf("2022-02-02");
        Integer experience = 5;
        String job = "testJob";

        PersonalDto personalDto = PersonalDto.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toLocalDate())
                .employmentDate(employmentDate.toLocalDate())
                .experience(experience).build();

        personalService.createPersonal(personalDto);

        List<Personal> personalList = personalRepository.findByNameSurnameAndBirthDate(name, surname, birthDate);

        Personal personalSaved = personalList.getLast();

        DepartmentDto departmentDto = DepartmentDto.builder()
                .name("testDepartment").build();

        departmentService.createDepartment(departmentDto);

        Department departmentSaved = departmentRepository.findByName("testDepartment");

        PersonalJob personalJob = PersonalJob.builder()
                .personal(personalSaved)
                .jobTitle(job)
                .department(departmentSaved).build();

        PersonalJobDto personalJobDto = personalJobService.personalJobToDto(personalJob);

        Assertions.assertNotNull(personalSaved);
        Assertions.assertNotNull(departmentSaved);
        Assertions.assertEquals(personalJobDto.getPersonal().getName(), name);
        Assertions.assertEquals(personalJobDto.getPersonal().getSurname(), surname);
        Assertions.assertEquals(personalJobDto.getPersonal().getBirthDate(), birthDate.toLocalDate());
        Assertions.assertEquals(personalJobDto.getPersonal().getEmploymentDate(), employmentDate.toLocalDate());

    }


    public void dtoToPersonalJobWithCreatePersonal() {
        Assertions.assertNotNull(personalJobService);

        Assertions.assertNotNull(personalJobService);
        Assertions.assertNotNull(personalService);
        Assertions.assertNotNull(departmentService);
        String name = "testName";
        String surname = "testSurname";
        Date birthDate = Date.valueOf("2000-02-02");
        Date employmentDate = Date.valueOf("2022-02-02");
        Integer experience = 5;
        String job = "testJob";

        PersonalDto personalDto = PersonalDto.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toLocalDate())
                .employmentDate(employmentDate.toLocalDate())
                .experience(experience).build();

        DepartmentDto departmentDto = DepartmentDto.builder()
                .name("testDepartment").build();

        departmentService.createDepartment(departmentDto);
        DepartmentDto departmentDtoSaved = departmentService.findByName("testDepartment");
        PersonalJobDto personalJobDto = PersonalJobDto.builder()
                .personal(personalDto)
                .jobTitle(job)
                .department(departmentDtoSaved).build();

        PersonalJob personalJob = personalJobService.dtoToPersonalJobWithCreatePersonal(personalJobDto);

        Assertions.assertNotNull(departmentDtoSaved);
        Assertions.assertEquals(personalJobDto.getPersonal().getName(), name);
        Assertions.assertEquals(personalJobDto.getPersonal().getSurname(), surname);
        Assertions.assertEquals(personalJobDto.getPersonal().getBirthDate(), birthDate.toLocalDate());
        Assertions.assertEquals(personalJobDto.getPersonal().getEmploymentDate(), employmentDate.toLocalDate());


    }

    @Test
    public void findById() {
        Assertions.assertNotNull(personalJobService);
        Assertions.assertNotNull(personalService);
        Assertions.assertNotNull(departmentService);
        String name = "testName";
        String surname = "testSurname";
        Date birthDate = Date.valueOf("2000-02-02");
        Date employmentDate = Date.valueOf("2022-02-02");
        Integer experience = 5;
        String job = "testJob";

        PersonalDto personalDto = PersonalDto.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toLocalDate())
                .employmentDate(employmentDate.toLocalDate())
                .experience(experience).build();

        DepartmentDto departmentDto = DepartmentDto.builder()
                .name("testDepartment").build();

        departmentService.createDepartment(departmentDto);
        DepartmentDto departmentDtoSaved = departmentService.findByName("testDepartment");

        PersonalJobDto personalJobDto = PersonalJobDto.builder()
                .personal(personalDto)
                .jobTitle(job)
                .department(departmentDtoSaved).build();

        personalJobService.createPersonalJob(personalJobDto);

        List<PersonalJob> personalJobList = personalJobRepository.findByNameSurnameJob(name, surname, job);
        PersonalJob personalJob = personalJobList.getLast();
        Integer personalJobId = personalJob.getId();

        PersonalJob personalJobFoundedById = personalJobService.findById(personalJobId);

        Assertions.assertNotNull(personalJob);
        Assertions.assertNotNull(personalJobId);
        Assertions.assertNotNull(personalJobFoundedById);
        Assertions.assertEquals(personalJobFoundedById.getPersonal().getName(), name);
        Assertions.assertEquals(personalJobFoundedById.getPersonal().getSurname(), surname);

    }


    @Test
    public void createPersonalJob() {

        Assertions.assertNotNull(personalJobService);
        Assertions.assertNotNull(personalService);
        Assertions.assertNotNull(departmentService);
        String name = "testName";
        String surname = "testSurname";
        Date birthDate = Date.valueOf("2000-02-02");
        Date employmentDate = Date.valueOf("2022-02-02");
        Integer experience = 5;
        String job = "testJob";

        PersonalDto personalDto = PersonalDto.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toLocalDate())
                .employmentDate(employmentDate.toLocalDate())
                .experience(experience).build();

        DepartmentDto departmentDto = DepartmentDto.builder()
                .name("testDepartment").build();

        departmentService.createDepartment(departmentDto);
        DepartmentDto departmentDtoSaved = departmentService.findByName("testDepartment");

        PersonalJobDto personalJobDto = PersonalJobDto.builder()
                .personal(personalDto)
                .jobTitle(job)
                .department(departmentDtoSaved).build();

        personalJobService.createPersonalJob(personalJobDto);
        List<PersonalJobDto> personalJobDtoList = personalJobService.findByNameSurnameJob(name, surname, job);
        PersonalJobDto personalJobDtoSaved = personalJobDtoList.getLast();

        Assertions.assertNotNull(personalJobDtoSaved);
        Assertions.assertEquals(personalJobDtoSaved.getJobTitle(), job);
        Assertions.assertEquals(personalJobDtoSaved.getDepartment().getName(), departmentDtoSaved.getName());


    }

}
