package com.medCenter.medCenter;

import com.medCenter.medCenter.dto.DepartmentDto;
import com.medCenter.medCenter.dto.PersonalDto;
import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ScheduleDto;
import com.medCenter.medCenter.exception.ScheduleExistException;
import com.medCenter.medCenter.model.entity.Personal;
import com.medCenter.medCenter.model.entity.Schedule;
import com.medCenter.medCenter.model.repository.PersonalJobRepository;
import com.medCenter.medCenter.model.repository.ScheduleRepository;
import com.medCenter.medCenter.service.DepartmentService;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.PersonalService;
import com.medCenter.medCenter.service.ScheduleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@Transactional
public class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private PersonalService personalService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PersonalJobService personalJobService;
    @Autowired
    private PersonalJobRepository personalJobRepository;


    private PersonalJobDto createSubInstances() {
        String name = "testName";
        String surname = "testSurname";
        Date birthDate = Date.valueOf("2000-02-02");
        Date employmentDate = Date.valueOf("2022-02-02");
        Integer experience = 10;
        String job = "testJob";
        String departmentName = "testDepartment";

        DepartmentDto departmentDto = DepartmentDto.builder()
                .name(departmentName).build();

        departmentService.createDepartment(departmentDto);
        departmentDto = departmentService.findByName(departmentName);

        PersonalDto personalDto = PersonalDto.builder()
                .name(name)
                .surname(surname)
                .birthDate(birthDate.toLocalDate())
                .employmentDate(employmentDate.toLocalDate())
                .experience(experience)
                .build();

        PersonalJobDto personalJobDto = PersonalJobDto.builder()
                .personal(personalDto)
                .jobTitle(job)
                .department(departmentDto).build();

        personalJobService.createPersonalJob(personalJobDto);
        List<PersonalJobDto> personalJobDtoList = personalJobService.findByNameSurnameJob(name, surname, job);

        return personalJobDtoList.getLast();
    }


    @Test
    public void findByPeriodAndPersonalId() throws ScheduleExistException {
        Assertions.assertNotNull(scheduleService);
        PersonalJobDto personalJobDto = createSubInstances();

        Assertions.assertNotNull(personalJobDto);

        LocalDate date1 = LocalDate.of(2025, 5, 5);
        LocalDate date2 = LocalDate.of(2025, 5, 6);
        LocalDate date3 = LocalDate.of(2025, 5, 7);
        LocalDate date4 = LocalDate.of(2025, 5, 8);
        LocalDate date5 = LocalDate.of(2025, 5, 9);

        LocalTime time1 = LocalTime.of(8, 0, 0);
        LocalTime time2 = LocalTime.of(14, 0, 0);

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(date1)
                .startTime(time1)
                .endTime(time2)
                .personalJob(personalJobDto).build();

        scheduleService.setScheduleForDay(scheduleDto);
        scheduleDto.setDate(date2);
        scheduleService.setScheduleForDay(scheduleDto);
        scheduleDto.setDate(date3);
        scheduleService.setScheduleForDay(scheduleDto);
        scheduleDto.setDate(date4);
        scheduleService.setScheduleForDay(scheduleDto);
        scheduleDto.setDate(date5);
        scheduleService.setScheduleForDay(scheduleDto);

        List<ScheduleDto> scheduleDtoList = scheduleService.findByPeriodAndPersonalId(date1.toString(), date5.toString(), String.valueOf(personalJobDto.getId()));
        System.out.println("SCHEDULE LIST " + scheduleDtoList);
        Assertions.assertNotNull(scheduleDtoList);
        Assertions.assertEquals(scheduleDtoList.size(), 5);

    }


    @Test
    public void setScheduleForDay() throws ScheduleExistException {

        Assertions.assertNotNull(scheduleService);

        PersonalJobDto personalJobDto = createSubInstances();

        Assertions.assertNotNull(personalJobDto);

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(LocalDate.of(2025, 5, 5))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(14, 0))
                .personalJob(personalJobDto).build();

        scheduleService.setScheduleForDay(scheduleDto);

        List<ScheduleDto> scheduleDtoList = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoSaved = scheduleDtoList.getLast();

        Assertions.assertNotNull(scheduleDtoSaved);


    }

    @Test
    public void setScheduleWithFixedTimeForPeriod() {

        Assertions.assertNotNull(scheduleService);
        PersonalJobDto personalJobDto = createSubInstances();
        Assertions.assertNotNull(personalJobDto);
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(LocalDate.of(2025, 5, 5))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(14, 0))
                .personalJob(personalJobDto).build();

        String date1 = "2025-05-05";
        String date2 = "2025-05-10";
        String time1 = "08:00:00";
        String time2 = "14:00:00";
        String personalJobId = String.valueOf(personalJobDto.getId());

        scheduleService.setScheduleWithFixedTimeForPeriod(date2, scheduleDto);

        List<ScheduleDto> scheduleDtoList = scheduleService.findByPeriodAndPersonalId(date1, date2, personalJobId);
        System.out.println("SCHEDULE LIST " + scheduleDtoList);
        System.out.println("SCHEDULE LIST SIZE " + scheduleDtoList.size());
        Assertions.assertNotNull(scheduleDtoList);
        Assertions.assertEquals(scheduleDtoList.size(), 6);
    }

    @Test
    public void setDayOff() throws ScheduleExistException {
        Assertions.assertNotNull(scheduleService);
        PersonalJobDto personalJobDto = createSubInstances();
        Assertions.assertNotNull(personalJobDto);

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(LocalDate.of(2025, 5, 5))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(14, 0))
                .personalJob(personalJobDto).build();

        scheduleService.setScheduleForDay(scheduleDto);

        List<ScheduleDto> scheduleDtoList = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoSaved = scheduleDtoList.getLast();

        Assertions.assertNotNull(scheduleDtoSaved);
        System.out.println("SCHEDULE SAVED" + scheduleDtoSaved);
        Integer scheduleId = scheduleDtoSaved.getId();

        System.out.println("SCHEDULE ID" + scheduleId);

        scheduleService.setDayOff("dayOffTest", scheduleId);

        List<ScheduleDto> scheduleDtoList1 = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoUpdated = scheduleDtoList1.getLast();
        System.out.println("SCHEDULE UPDATED" + scheduleDtoUpdated);

        Assertions.assertNotNull(scheduleDtoUpdated);
        Assertions.assertEquals(scheduleDtoUpdated.getId(), scheduleDtoSaved.getId());
        Assertions.assertEquals(scheduleDtoUpdated.getDayOff(), "dayOffTest");
        Assertions.assertNull(scheduleDtoUpdated.getStartTime());
        Assertions.assertNull(scheduleDtoUpdated.getEndTime());

    }

    @Test
    public void updateTime() throws ScheduleExistException {
        Assertions.assertNotNull(scheduleService);
        PersonalJobDto personalJobDto = createSubInstances();
        Assertions.assertNotNull(personalJobDto);

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(LocalDate.of(2025, 5, 5))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(14, 0))
                .personalJob(personalJobDto).build();

        scheduleService.setScheduleForDay(scheduleDto);

        List<ScheduleDto> scheduleDtoList = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoSaved = scheduleDtoList.getLast();

        Assertions.assertNotNull(scheduleDtoSaved);
        System.out.println("SCHEDULE SAVED" + scheduleDtoSaved);
        Integer scheduleId = scheduleDtoSaved.getId();
        System.out.println("SCHEDULE ID" + scheduleId);

        Time time1 = Time.valueOf("11:00:00");
        Time time2 = Time.valueOf("12:00:00");

        scheduleService.updateTime(time1, time2, scheduleId);
        List<ScheduleDto> scheduleDtoList1 = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoUpdated = scheduleDtoList1.getLast();
        System.out.println("SCHEDULE UPDATED" + scheduleDtoUpdated);

        Assertions.assertNotNull(scheduleDtoUpdated);
        Assertions.assertEquals(scheduleDtoUpdated.getId(), scheduleDtoSaved.getId());
        Assertions.assertEquals(scheduleDtoUpdated.getStartTime(), time1.toLocalTime());
        Assertions.assertEquals(scheduleDtoUpdated.getEndTime(), time2.toLocalTime());
        Assertions.assertNull(scheduleDtoUpdated.getDayOff());


    }

    @Test
    public void updateStartTime() throws ScheduleExistException {

        Assertions.assertNotNull(scheduleService);
        PersonalJobDto personalJobDto = createSubInstances();
        Assertions.assertNotNull(personalJobDto);

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(LocalDate.of(2025, 5, 5))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(14, 0))
                .personalJob(personalJobDto).build();

        scheduleService.setScheduleForDay(scheduleDto);

        List<ScheduleDto> scheduleDtoList = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoSaved = scheduleDtoList.getLast();

        Assertions.assertNotNull(scheduleDtoSaved);
        System.out.println("SCHEDULE SAVED" + scheduleDtoSaved);
        Integer scheduleId = scheduleDtoSaved.getId();
        System.out.println("SCHEDULE ID" + scheduleId);

        Time time1 = Time.valueOf("11:00:00");

        scheduleService.updateStartTime(time1, scheduleId);
        List<ScheduleDto> scheduleDtoList1 = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoUpdated = scheduleDtoList1.getLast();
        System.out.println("SCHEDULE UPDATED" + scheduleDtoUpdated);

        Assertions.assertNotNull(scheduleDtoUpdated);
        Assertions.assertEquals(scheduleDtoUpdated.getId(), scheduleDtoSaved.getId());
        Assertions.assertEquals(scheduleDtoUpdated.getStartTime(), time1.toLocalTime());
        Assertions.assertEquals(scheduleDtoUpdated.getEndTime(), scheduleDtoSaved.getEndTime());
        Assertions.assertNull(scheduleDtoUpdated.getDayOff());


    }

    @Test
    public void updateEndTime() throws ScheduleExistException {
        Assertions.assertNotNull(scheduleService);
        PersonalJobDto personalJobDto = createSubInstances();
        Assertions.assertNotNull(personalJobDto);

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(LocalDate.of(2025, 5, 5))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(14, 0))
                .personalJob(personalJobDto).build();

        scheduleService.setScheduleForDay(scheduleDto);

        List<ScheduleDto> scheduleDtoList = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoSaved = scheduleDtoList.getLast();

        Assertions.assertNotNull(scheduleDtoSaved);
        System.out.println("SCHEDULE SAVED" + scheduleDtoSaved);
        Integer scheduleId = scheduleDtoSaved.getId();
        System.out.println("SCHEDULE ID" + scheduleId);

        Time time = Time.valueOf("12:00:00");

        scheduleService.updateEndTime(time, scheduleId);
        List<ScheduleDto> scheduleDtoList1 = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoUpdated = scheduleDtoList1.getLast();
        System.out.println("SCHEDULE UPDATED" + scheduleDtoUpdated);

        Assertions.assertNotNull(scheduleDtoUpdated);
        Assertions.assertEquals(scheduleDtoUpdated.getId(), scheduleDtoSaved.getId());
        Assertions.assertEquals(scheduleDtoUpdated.getStartTime(), scheduleDtoSaved.getStartTime());
        Assertions.assertEquals(scheduleDtoUpdated.getEndTime(), time.toLocalTime());
        Assertions.assertNull(scheduleDtoUpdated.getDayOff());


    }

    @Test
    public void updateState() throws ScheduleExistException {
        Assertions.assertNotNull(scheduleService);
        PersonalJobDto personalJobDto = createSubInstances();
        Assertions.assertNotNull(personalJobDto);

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .date(LocalDate.of(2025, 5, 5))
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(14, 0))
                .personalJob(personalJobDto).build();

        scheduleService.setScheduleForDay(scheduleDto);

        List<ScheduleDto> scheduleDtoList = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoSaved = scheduleDtoList.getLast();

        Assertions.assertNotNull(scheduleDtoSaved);
        System.out.println("SCHEDULE SAVED" + scheduleDtoSaved);
        Integer scheduleId = scheduleDtoSaved.getId();
        System.out.println("SCHEDULE ID" + scheduleId);

        String state = "testState";

        scheduleService.updateState(state, scheduleId);
        List<ScheduleDto> scheduleDtoList1 = scheduleService.findByDateAndPersonalId(personalJobDto.getId().toString(), scheduleDto.getDate().toString());
        ScheduleDto scheduleDtoUpdated = scheduleDtoList1.getLast();
        System.out.println("SCHEDULE UPDATED" + scheduleDtoUpdated);

        Assertions.assertNotNull(scheduleDtoUpdated);
        Assertions.assertEquals(scheduleDtoUpdated.getId(), scheduleDtoSaved.getId());
        Assertions.assertEquals(scheduleDtoUpdated.getState(), state);

    }

}
